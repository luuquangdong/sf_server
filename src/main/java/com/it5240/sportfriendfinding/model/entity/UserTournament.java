package com.it5240.sportfriendfinding.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Document
public class UserTournament {
    @Id
    private String userId;
    private Set<ObjectId> tournamentIdsJoined;
    private Set<ObjectId> myTournamentIds;

    public UserTournament(){
    	tournamentIdsJoined = new HashSet<>();
    	myTournamentIds = new HashSet<>();
    }

    public UserTournament(String userId){
        this();
        this.userId = userId;
    }

}
