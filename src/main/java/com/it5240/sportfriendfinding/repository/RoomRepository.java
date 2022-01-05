package com.it5240.sportfriendfinding.repository;

import com.it5240.sportfriendfinding.model.entity.Room;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, ObjectId> {
    List<Room> findByUserIds(String userId);
}
