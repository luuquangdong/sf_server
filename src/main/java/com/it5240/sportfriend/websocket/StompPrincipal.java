package com.it5240.sportfriend.websocket;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private String userId;

    public StompPrincipal(String userId){
        this.userId = userId;
    }

    @Override
    public String getName() {
        return this.userId;
    }
}