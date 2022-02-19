package com.it5240.sportfriend.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
    @JsonSerialize(using = ToStringSerializer.class)
    private Set<ObjectId> tournamentIdsJoined;
    @JsonSerialize(using = ToStringSerializer.class)
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
