package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByPhoneNumberIn(List<String> userIds);
}