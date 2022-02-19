package com.it5240.sportfriend.repository;

import com.it5240.sportfriend.model.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, ObjectId> {
    List<Message> findByRoomId(ObjectId roomId);
    List<Message> findByRoomId(ObjectId roomId, Pageable pageable);
    List<Message> findByRoomIdAndIdLessThan(ObjectId roomId, ObjectId lastMessageId, Pageable pageable);
    List<Message> findByRoomIdAndRead(ObjectId roomId, boolean read);
    void deleteByRoomId(ObjectId roomId);
}
