package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.unit.TournamentStatus;
import com.it5240.sportfriend.model.entity.Tournament;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends MongoRepository<Tournament, ObjectId> {
    List<Tournament> findByStatus(TournamentStatus status, Pageable pageable);
    List<Tournament> findByStatusAndIdNotIn(TournamentStatus status, List<ObjectId> notInIds, Pageable pageable);
    List<Tournament> findByStatusAndIdLessThan(TournamentStatus status, ObjectId id, Pageable pageable);
    List<Tournament> findByStatusAndIdLessThanAndIdNotIn(TournamentStatus status, ObjectId id, List<ObjectId> notInIds, Pageable pageable);
    List<Tournament> findByIdIn(List<ObjectId> Ids);
}
