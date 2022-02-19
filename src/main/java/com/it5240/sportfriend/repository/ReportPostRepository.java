package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.entity.ReportPost;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportPostRepository extends MongoRepository<ReportPost, ObjectId> {
}
