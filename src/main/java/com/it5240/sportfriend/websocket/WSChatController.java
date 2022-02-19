package com.it5240.sportfriend.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WSChatController {
    @Autowired
    private WSChatService chatWSService;

    @MessageMapping("/message")
    public void sendMessage(SimpMessageHeaderAccessor sha, @Payload String messageJson){
        chatWSService.sendMessage(messageJson);
    }

    @MessageMapping("/first-message")
    public void sendFirstMessage(SimpMessageHeaderAccessor sha, @Payload String jsonString){
        chatWSService.sendFirstMessage(jsonString);
    }
}
