package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.model.dto.post.Author;
import com.it5240.sportfriendfinding.model.dto.post.CommentResp;
import com.it5240.sportfriendfinding.model.entity.Comment;
import com.it5240.sportfriendfinding.model.entity.Post;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.CommentRepository;
import com.it5240.sportfriendfinding.repository.PostRepository;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.utils.RespHelper;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;

    public CommentResp createComment(Comment newComment, String authorId){
        Post post = postRepository.findById(newComment.getPostId())
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.POST_NOT_FOUND));

        newComment.setAuthorId(authorId);
        Comment commentSaved = commentRepository.save(newComment);

        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        CommentResp response = comment2Response(commentSaved, authorId, null);

        return response;
    }

    public String deleteComment(ObjectId commentId, String authorId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.COMMENT_NOT_FOUND)
                );

        if(!comment.getAuthorId().equals(authorId)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        commentRepository.deleteById(commentId);
        Post post = postRepository.findById(comment.getPostId()).get();
        post.setCommentCount(post.getCommentCount() - 1);
        postRepository.save(post);

        return RespHelper.ok();
    }

    public List<CommentResp> getListComment(ObjectId postId, String meId, int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        List<Comment> comments = commentRepository.findByPostId(postId, paging);

        Map<String, Author> authorCache = new HashMap<>();
        List<CommentResp> result = comments
                .stream()
                .map(comment -> comment2Response(comment, meId, authorCache))
                .collect(Collectors.toList());
        return result;
    }

    private CommentResp comment2Response(Comment comment, String meId, Map<String, Author> authorCache){
        CommentResp response = modelMapper.map(comment, CommentResp.class);

        Author authorComment = null;
        if(authorCache != null && authorCache.containsKey(meId)){
            authorComment = authorCache.get(meId);
        } else {
            User user = userRepository.findById(comment.getAuthorId()).get();
            authorComment = new Author(user.getPhoneNumber(), user.getName(), user.getAvatar());

            if(authorCache != null){
                authorCache.put(authorComment.getId(), authorComment);
            }
        }
        response.setAuthor(authorComment);
        response.setCanEdit(comment.getAuthorId().equals(meId));

        return response;
    }

}
