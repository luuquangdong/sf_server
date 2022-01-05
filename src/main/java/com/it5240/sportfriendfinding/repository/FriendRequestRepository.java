package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.FriendRequest;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, ObjectId> {
    List<FriendRequest> findByUserIdReceive(String userId);
}
