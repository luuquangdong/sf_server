package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, ObjectId> {
    public List<Comment> findByPostId(ObjectId postId, Pageable pageable);
}
