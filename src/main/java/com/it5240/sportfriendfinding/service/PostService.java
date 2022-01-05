package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.exception.model.InvalidException;
import com.it5240.sportfriendfinding.model.atom.Media;
import com.it5240.sportfriendfinding.model.dto.post.Author;
import com.it5240.sportfriendfinding.model.dto.post.PostReq;
import com.it5240.sportfriendfinding.model.dto.post.PostResp;
import com.it5240.sportfriendfinding.model.entity.Post;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.PostRepository;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.utils.RespHelper;
import com.it5240.sportfriendfinding.utils.Uploader;
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

    public PostResp createPost(PostReq newPost, String phoneNumber){
        if(newPost.getContent() == null && newPost.getImage() == null && newPost.getVideo() == null){
            throw InvalidExceptionFactory.get(ExceptionType.PARAMETER_NOT_ENOUGH);
        }

        if(newPost.getImage() != null && newPost.getVideo() != null){
            throw new InvalidException("Just either a image or a video", 1006);
        }

        Post post = new Post();
        post.setAuthorId(phoneNumber);
        post.setContent(newPost.getContent());
        post.setBanned(false);
        post.setCanComment(true);
        post.setUserLikedId(new TreeSet<>());

        if(newPost.getImage() != null){
            Media image = uploader.uploadImage(newPost.getImage(), "image");
            post.setImage(image);
        }

        if(newPost.getVideo() != null){
            Media video = uploader.uploadVideo(newPost.getVideo(), "video");
            post.setVideo(video);
        }

        Post createdPost = postRepository.save(post);

//        User user = userRepository.findById(phoneNumber).get();
//
//        PostResponse response = mapper.map(createdPost, PostResponse.class);
//        response.setCanEdit(true);
//        response.setAuthor(new Author(user.getPhoneNumber(), user.getName(), user.getAvatar()));
        PostResp response = post2Response(createdPost, phoneNumber, new HashMap<String, Author>());

        return response;
    }

    public List<PostResp> getListPost(String phoneNumber, ObjectId lastPostId, int size){
        User user = userRepository.findById(phoneNumber).get();
        List<String> authorIds = new ArrayList<>(user.getFriendIds());
        authorIds.add(phoneNumber);

        Map<String, Author> authorCache = new HashMap<>();
        authorCache.put(phoneNumber, new Author(phoneNumber, user.getName(), user.getAvatar()));

        Pageable paging = PageRequest.of(0, size, Sort.by("lastModifiedDate").descending());
        List<Post> posts = null;
        if(lastPostId == null){
            posts = postRepository.findByAuthorIdIn(authorIds, paging);
        } else {
            posts = postRepository.findByAuthorIdInAndIdLessThan(authorIds, lastPostId, paging);
        }
//        List<Post> posts = postRepository.findByAuthorIdIn(authorIds, paging);
        return posts
                .stream()
                .map(post -> post2Response(post, phoneNumber, authorCache))
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

    public String deletePost(String phoneNumber, ObjectId postId){
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
}