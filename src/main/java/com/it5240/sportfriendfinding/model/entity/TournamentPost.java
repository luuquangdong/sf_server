package com.it5240.sportfriendfinding.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Document
public class TournamentPost {
    @Id
    private ObjectId tournamentId;
    private List<ObjectId> postIds;

    public TournamentPost(){
        postIds = new ArrayList<>();
    }

    public TournamentPost(ObjectId tournamentId){
        this.tournamentId = tournamentId;
        postIds = new ArrayList<>();
    }

    public TournamentPost(ObjectId tournamentId, ObjectId postId){
        this.tournamentId = tournamentId;
        this.postIds = List.of(postId);
    }

    public void addPostId(ObjectId postId){
        postIds.add(postId);
    }

    public void removePostId(ObjectId postId){
        postIds.remove(postId);
    }
}
