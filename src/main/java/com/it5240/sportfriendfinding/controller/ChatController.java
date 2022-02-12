package com.it5240.sportfriendfinding.controller;

import com.it5240.sportfriendfinding.model.dto.chat.RoomResp;
import com.it5240.sportfriendfinding.model.entity.Message;
import com.it5240.sportfriendfinding.model.entity.Room;
import com.it5240.sportfriendfinding.service.ChatService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages(
            @RequestParam(required = true) ObjectId roomId,
            @RequestParam(required = false) ObjectId lastMessageId,
            @RequestParam(defaultValue = "20") int size
    ){
        List<Message> messages = chatService.getListMessage(roomId, lastMessageId, size);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@Valid @RequestBody Message message){
        Message messageCreated = chatService.createMessage(message);
        return ResponseEntity.ok(messageCreated);
    }

    @GetMapping("/messages/new/{roomId}")
    public ResponseEntity<?> getNewMessage(@PathVariable ObjectId roomId){
        List<Message> messages = chatService.getNewMessage(roomId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/rooms/{userId}")
    public ResponseEntity<?> getListRoom(@PathVariable String userId){
        List<RoomResp> result = chatService.getListRoom(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room room){
        room.setId(null);
        RoomResp roomCreated = chatService.createRoom(room);
        return ResponseEntity.ok(roomCreated);
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<?> getDeleteRoom(@PathVariable ObjectId roomId, Principal principal){
        String meId = principal.getName();
        var result = chatService.deleteRoom(roomId, meId);
        return ResponseEntity.ok(result);
    }
}
