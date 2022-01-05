package com.it5240.sportfriendfinding.repository.dao;

import com.it5240.sportfriendfinding.model.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Message> findByRoomId(ObjectId roomId, int index, int size){
        Criteria criteria = Criteria.where("roomId").is(roomId);
        Query query = Query.query(criteria);
        query.with(Sort.by(Sort.Direction.DESC, "createdTime"));
        query.skip(index);
        query.limit(size);
        return mongoTemplate.find(query, Message.class);
    }

}
