package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.entity.UserTournament;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTournamentRepository extends MongoRepository<UserTournament, String> {

}
