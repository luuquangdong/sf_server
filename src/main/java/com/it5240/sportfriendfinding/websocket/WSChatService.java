package com.it5240.sportfriendfinding.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it5240.sportfriendfinding.model.dto.chat.RoomResp;
import com.it5240.sportfriendfinding.model.entity.Message;
import com.it5240.sportfriendfinding.model.entity.Room;
import com.it5240.sportfriendfinding.repository.MessageRepository;
import com.it5240.sportfriendfinding.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSChatService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String messageJson){
        try {
            Message message = objectMapper.readValue(messageJson, Message.class);

            simpMessagingTemplate.convertAndSendToUser(message.getReceiveId(), "/queue/messages", message);
            Room room = roomRepository.findById(message.getRoomId()).get();
            messageRepository.save(message);
            roomRepository.save(room);
        } catch (MessagingException me){
            System.err.println("Can't send message");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void sendFirstMessage(String messageJson){
        try {
            RoomResp roomResp = objectMapper.readValue(messageJson, RoomResp.class);
            Message message = roomResp.getMessages().get(0);

            simpMessagingTemplate.convertAndSendToUser(message.getReceiveId(), "/queue/first-message", roomResp);
            messageRepository.save(message);
        } catch (MessagingException me){
            System.err.println("Can't send message");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
