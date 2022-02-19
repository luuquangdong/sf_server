package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.entity.SignupOrganization;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignupOrganizationRepository extends MongoRepository<SignupOrganization, ObjectId> {
}
