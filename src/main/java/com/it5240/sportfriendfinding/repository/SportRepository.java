package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.Sport;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends MongoRepository<Sport, ObjectId> {

    public List<Sport> findByIdIn(List<ObjectId> sportIds);

}
