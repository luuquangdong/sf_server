package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.exception.InvalidException;
import com.it5240.sportfriend.model.unit.Media;
import com.it5240.sportfriend.model.unit.Site;
import com.it5240.sportfriend.model.dto.post.Author;
import com.it5240.sportfriend.model.dto.post.PostReq;
import com.it5240.sportfriend.model.dto.post.PostResp;
import com.it5240.sportfriend.model.dto.tournament.TournamentPostReq;
import com.it5240.sportfriend.model.entity.Post;
import com.it5240.sportfriend.model.entity.ReportPost;
import com.it5240.sportfriend.model.entity.TournamentPost;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.PostRepository;
import com.it5240.sportfriend.repository.ReportPostRepository;
import com.it5240.sportfriend.repository.TournamentPostRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.utils.RespHelper;
import com.it5240.sportfriend.utils.Uploader;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Uploader uploader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ReportPostRepository reportPostRepository;

    @Autowired
    private TournamentPostRepository tournamentPostRepository;

    public PostResp createPost(PostReq newPost, String phoneNumber){
        if(newPost.getContent() == null && newPost.getImage() == null && newPost.getVideo() == null){
            throw InvalidExceptionFactory.get(ExceptionType.PARAMETER_NOT_ENOUGH);
        }

        if(newPost.getImage() != null && newPost.getVideo() != null){
            throw new InvalidException("Just either a image or a video", 1006);
        }

        Post post = new Post(phoneNumber, newPost.getContent(), Site.HOME);

        if(newPost.getImage() != null){
            Media image = uploader.uploadImage(newPost.getImage(), "image");
            post.setImage(image);
        }

        if(newPost.getVideo() != null){
            Media video = uploader.uploadVideo(newPost.getVideo(), "video");
            post.setVideo(video);
        }

        Post createdPost = postRepository.save(post);

        PostResp response = post2Response(createdPost, phoneNumber, null);

        return response;
    }

    public PostResp getPost(ObjectId postId){
        Post post =  postRepository.findById(postId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND));
        User u = userRepository.findById(post.getAuthorId()).orElse(new User());
        PostResp postResp = mapper.map(post, PostResp.class);

        postResp.setAuthor(new Author(u.getPhoneNumber(), u.getName(), u.getAvatar()));

        return postResp;
    }

    public List<PostResp> getListPost(String phoneNumber, ObjectId lastId, int size){
        User user = userRepository.findById(phoneNumber).get();
        List<String> authorIds = new ArrayList<>(user.getFriendIds());
        authorIds.add(phoneNumber);

        Map<String, Author> authorCache = new HashMap<>();
        authorCache.put(phoneNumber, new Author(phoneNumber, user.getName(), user.getAvatar()));

        Pageable paging = PageRequest.of(0, size, Sort.by("id").descending());
        List<Post> posts = null;
        if(lastId == null){
//            posts = postRepository.findByAuthorIdIn(authorIds, paging);
            posts = postRepository.findByAuthorIdInAndSite(authorIds, Site.HOME, paging);
        } else {
//            posts = postRepository.findByAuthorIdInAndIdLessThan(authorIds, lastId, paging);
            posts = postRepository.findByAuthorIdInAndIdLessThanAndSite(authorIds, lastId, Site.HOME, paging);
        }
//        List<Post> posts = postRepository.findByAuthorIdIn(authorIds, paging);
        return posts
                .stream()
                .map(post -> post2Response(post, phoneNumber, authorCache))
                .collect(Collectors.toList());
    }

    public List<PostResp> getListPostOfUser(String posterId, String meId){
        List<Post> posts = postRepository.findByAuthorIdAndSite(posterId, Site.HOME);

        User poster = userRepository.findById(posterId).get();
        Map<String, Author> authorCache = new HashMap<>();
        authorCache.put(posterId, new Author(posterId, poster.getName(), poster.getAvatar()));

        return posts.stream()
                .map(p -> post2Response(p, meId, authorCache))
                .collect(Collectors.toList());
    }

    private PostResp post2Response(Post post, String phoneNumber, Map<String, Author> authorCache){
        PostResp postResp = mapper.map(post, PostResp.class);
        postResp.setCanEdit(post.getAuthorId().equals(phoneNumber));

        Author author = null;
        if(authorCache != null && authorCache.containsKey(post.getAuthorId())){
            author = authorCache.get(post.getAuthorId());
        } else {
            User u = userRepository.findById(post.getAuthorId()).orElse(new User());
            author = new Author(post.getAuthorId(), u.getName(), u.getAvatar());

            if(authorCache != null) {
                authorCache.put(post.getAuthorId(), author);
            }
        }
        postResp.setAuthor(author);

        postResp.setLiked(post.getUserLikedId().contains(phoneNumber));
        postResp.setLikeCount(post.getUserLikedId().size());

        return postResp;
    }

    public Map<String, Object> deletePost(String phoneNumber, ObjectId postId){
        Optional<Post> postOpt = postRepository.findById(postId);
        if(postOpt.isEmpty()){
            throw NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND);
        }
        Post post = postOpt.get();

        if(!post.getAuthorId().equals(phoneNumber)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        if(post.getImage() != null){
            uploader.deleteFile(post.getImage().getId());
        }
        if(post.getVideo() != null){
            uploader.deleteFile(post.getVideo().getId());
        }
        postRepository.delete(post);

        return RespHelper.ok();
    }

    public PostResp updatePost(PostReq newPost, String phoneNumber){
        if(newPost.getVideo() != null && newPost.getImage() != null){
            throw new InvalidException("Just either a image or a video", 1006);
        }

        Optional<Post> postOpt = postRepository.findById(newPost.getId());
        if(postOpt.isEmpty()){
            throw NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND);
        }
        Post post = postOpt.get();

        if(!post.getAuthorId().equals(phoneNumber)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        post.setContent(newPost.getContent());

        if(newPost.getImage() != null){
           Media image = uploader.uploadImage(newPost.getImage(), "image");
           if(post.getImage() != null){
               uploader.deleteFile(post.getImage().getId());
           }
           post.setImage(image);

           if(post.getVideo() != null){
               uploader.deleteFile(post.getVideo().getId());
               post.setVideo(null);
           }
        }

        if(newPost.getVideo() != null){
            Media video = uploader.uploadVideo(newPost.getVideo(), "video");
            if(post.getVideo() != null){
                uploader.deleteFile(post.getVideo().getId());
            }
            post.setVideo(video);

            if(post.getImage() != null){
                uploader.deleteFile(post.getImage().getId());
                post.setImage(null);
            }
        }

        Post updatedPost = postRepository.save(post);

        PostResp postResponse = mapper.map(updatedPost, PostResp.class);
        postResponse.setCanEdit(true);
        User author = userRepository.findById(phoneNumber).get();
        postResponse.setAuthor(new Author(phoneNumber, author.getName(), author.getAvatar()));

        return postResponse;
    }

    public PostResp like(ObjectId postId, String likerId){
        Optional<Post> postOpt = postRepository.findById(postId);
        postOpt.orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND));

        Post post = postOpt.get();

        Set<String> likeIds = post.getUserLikedId();
        if(likeIds.contains(likerId)){
            likeIds.remove(likerId);
        } else {
            likeIds.add(likerId);
        }

        postRepository.save(post);

        return post2Response(post, likerId, new HashMap<String, Author>());
    }

    public PostResp createTournamentPost(TournamentPostReq newPost, String meId){
        if(newPost.getContent() == null && newPost.getImage() == null && newPost.getVideo() == null){
            throw InvalidExceptionFactory.get(ExceptionType.PARAMETER_NOT_ENOUGH);
        }

        if(newPost.getImage() != null && newPost.getVideo() != null){
            throw new InvalidException("Just either a image or a video", 1006);
        }

        Post post = new Post(meId, newPost.getContent(), Site.TOURNAMENT);

        if(newPost.getImage() != null){
            Media image = uploader.uploadImage(newPost.getImage(), "image");
            post.setImage(image);
        }

        if(newPost.getVideo() != null){
            Media video = uploader.uploadVideo(newPost.getVideo(), "video");
            post.setVideo(video);
        }

        post = postRepository.save(post);

        TournamentPost tournamentPost = tournamentPostRepository.findById(newPost.getTournamentId())
                .orElse(new TournamentPost(newPost.getTournamentId()));
        tournamentPost.addPostId(post.getId());
        tournamentPostRepository.save(tournamentPost);

        return post2Response(post, meId, null);
    }

    public List<PostResp> getListTournamentPost(ObjectId tournamentId, String meId){
        TournamentPost tournamentPost = tournamentPostRepository.findById(tournamentId).orElse(new TournamentPost());
        List<ObjectId> postIds = tournamentPost.getPostIds();

        User me = userRepository.findById(meId).get();
        Map<String, Author> authorCache = new HashMap<>();
        authorCache.put(meId, new Author(meId, me.getName(), me.getAvatar()));

        return postRepository.findByIdIn(postIds)
                .stream()
                .map(p -> post2Response(p, meId, authorCache))
                .collect(Collectors.toList());
    }

    public Map<String, Object> deleteTournamentPost(ObjectId tournamentId, ObjectId postId, String meId){
        TournamentPost tournamentPost = tournamentPostRepository.findById(tournamentId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.TOURNAMENT_POST_NOT_FOUND));
        tournamentPost.removePostId(postId);
        postRepository.deleteById(postId);
        return RespHelper.ok();
    }

    public Map<String, Object> reportPost(ReportPost report, String meId){
        reportPostRepository.save(report);

        return RespHelper.ok();
    }

    public List<ReportPost> getAllReport(){
        return reportPostRepository.findAll(Sort.by("id").descending());
    }

    public Map<String, Object> deleteReport(ObjectId reportId){
        reportPostRepository.deleteById(reportId);
        return RespHelper.ok();
    }

    public List<Post> getAll() {
        return postRepository.findAll(Sort.by("id").descending());
    }

    public Post banPost(ObjectId postId, boolean value){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND));
        post.setBanned(value);
        post = postRepository.save(post);
        return post;
    }
}
