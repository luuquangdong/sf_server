package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.entity.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    public List<Comment> findByPostId(ObjectId postId, Pageable pageable);
    public List<Comment> findByPostIdAndIdLessThan(ObjectId postId, ObjectId lastCommentId, Pageable pageable);
}
