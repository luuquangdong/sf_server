package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.TournamentPost;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TournamentPostRepository extends MongoRepository<TournamentPost, ObjectId> {
    List<TournamentPost> findByTournamentId(ObjectId tournamentId);
}
