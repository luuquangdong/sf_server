package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.UserTournament;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTournamentRepository extends MongoRepository<UserTournament, String> {

}
