package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.Post;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, ObjectId> {

    List<Post> findByAuthorIdIn(List<String> authorIds, Pageable pageable);
    List<Post> findByAuthorIdInAndIdLessThan(List<String> authorIds, ObjectId lastPostId, Pageable pageable);

}
