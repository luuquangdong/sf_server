package com.it5240.sportfriend.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.List;

@Component
public class WebSocketSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionListener.class.getName());

    @Autowired
    private OnlineUsers onlineUsers;

    @EventListener
    public void connectionEstablished(SessionConnectedEvent sce)
    {
        MessageHeaders msgHeaders = sce.getMessage().getHeaders();
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sce.getMessage());
        List<String> nativeHeaders = sha.getNativeHeader("userId");
        if( nativeHeaders != null )
        {
            String userId = nativeHeaders.get(0);
            onlineUsers.addUser(userId);
            if( logger.isDebugEnabled() )
            {
                logger.debug("User with " + userId + " connected");
            }
        }
        else
        {
            Principal princ = (Principal) msgHeaders.get("simpUser");
            String userId = princ.getName();
            onlineUsers.addUser(userId);
            if( logger.isDebugEnabled() )
            {
                logger.debug("User with " + userId + " connected");
            }
        }
    }

    @EventListener
    public void webSockectDisconnect(SessionDisconnectEvent sde)
    {
        MessageHeaders msgHeaders = sde.getMessage().getHeaders();
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(sde.getMessage());
        List<String> nativeHeaders = sha.getNativeHeader("userId");
        if( nativeHeaders != null )
        {
            String userId = nativeHeaders.get(0);
            onlineUsers.remove(userId);
            if( logger.isDebugEnabled() )
            {
                logger.debug("User with " + userId + " disconnected");
            }
        }
        else
        {
            Principal princ = (Principal) msgHeaders.get("simpUser");
            String userId = princ.getName();
            onlineUsers.remove(userId);
            if( logger.isDebugEnabled() )
            {
                logger.debug("User with " + userId + " disconnected");
            }
        }
    }

}
