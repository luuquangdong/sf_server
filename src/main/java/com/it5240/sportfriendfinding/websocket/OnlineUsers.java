package com.it5240.sportfriendfinding.websocket;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OnlineUsers {
    private Set<String> userIds;

    public OnlineUsers(){
        this.userIds = new HashSet<>();
    }

    public void addUser(String userId){
        this.userIds.add(userId);
    }

    public void remove(String userId){
        this.userIds.remove(userId);
    }

    public Set<String> getUserIdsOnLine(){
        return this.userIds;
    }
}
