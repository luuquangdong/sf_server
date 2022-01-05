package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.atom.TournamentStatus;
import com.it5240.sportfriendfinding.model.entity.Tournament;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, ObjectId> {
    List<Tournament> findByStatus(TournamentStatus status, Pageable pageable);
    List<Tournament> findByStatusAndIdLessThan(TournamentStatus status, ObjectId id, Pageable pageable);
}
