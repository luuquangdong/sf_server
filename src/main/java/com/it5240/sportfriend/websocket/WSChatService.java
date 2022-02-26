package com.it5240.sportfriend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it5240.sportfriend.model.dto.chat.RoomResp;
import com.it5240.sportfriend.model.entity.Message;
import com.it5240.sportfriend.model.entity.Room;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.MessageRepository;
import com.it5240.sportfriend.repository.RoomRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.utils.NotificationUtil;
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
    @Autowired
    private NotificationUtil notificationUtil;
    @Autowired
    private UserRepository userRepository;


    public void sendMessage(String messageJson){
        try {
            Message message = objectMapper.readValue(messageJson, Message.class);

            simpMessagingTemplate.convertAndSendToUser(message.getReceiveId(), "/queue/messages", message);

            Room room = roomRepository.findById(message.getRoomId()).get();
            messageRepository.save(message);
            roomRepository.save(room);

            User receiver = userRepository.findById(message.getReceiveId()).get();
            if(receiver.getPushToken() == null) return;
            User sender = userRepository.findById(message.getSenderId()).get();
            notificationUtil.sendMessage(receiver.getPushToken(), sender.getName(), message.getContent());

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

            User receiver = userRepository.findById(message.getReceiveId()).get();
            if(receiver.getPushToken() == null) return;
            User sender = userRepository.findById(message.getSenderId()).get();
            notificationUtil.sendMessage(receiver.getPushToken(), sender.getName(), message.getContent());
        } catch (MessagingException me){
            System.err.println("Can't send message");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
