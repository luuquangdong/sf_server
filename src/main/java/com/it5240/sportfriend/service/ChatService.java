package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.dto.chat.RoomResp;
import com.it5240.sportfriend.model.dto.post.Author;
import com.it5240.sportfriend.model.entity.Message;
import com.it5240.sportfriend.model.entity.Room;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.MessageRepository;
import com.it5240.sportfriend.repository.RoomRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.repository.dao.MessageDao;
import com.it5240.sportfriend.utils.RespHelper;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MessageDao messageDAO;
    @Autowired
    private UserRepository userRepository;

    public RoomResp createRoom(Room newRoom){
        newRoom.setId(null);
        newRoom = roomRepository.save(newRoom);
        RoomResp response = modelMapper.map(newRoom, RoomResp.class);
        List<Author> chatters = getChatters(newRoom.getUserIds(), new HashMap<String, Author>());
        response.setChatters(chatters);
        return response;
    }

    public Message createMessage(Message message){
        Room room = roomRepository.findById(message.getRoomId())
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.ROOM_NOT_FOUND));

        message = messageRepository.save(message);
        roomRepository.save(room);

        return message;
    }

    public List<RoomResp> getListRoom(String userId){
        User me = userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));

        Map<String, Author> authorCache = new HashMap<>();
        Author meAuthor = modelMapper.map(me, Author.class);
        meAuthor.setId(userId);
        authorCache.put(userId, meAuthor);

        List<Room> rooms = roomRepository.findByUserIds(userId);

        List<RoomResp> response = rooms.stream()
                .map(room -> {
                    RoomResp roomResponse = modelMapper.map(room, RoomResp.class);

//                    List<Message> messages = messageRepository.findByRoomId(room.getId());
                    List<Message> messageList = messageDAO.findByRoomId(room.getId(), 0, 20);
                    roomResponse.setMessages(messageList);

                    List<Author> chatters = getChatters(room.getUserIds(), authorCache);
                    roomResponse.setChatters(chatters);

                    return roomResponse;
                })
                .sorted(Comparator.comparing(Room::getLastModified).reversed())
                .collect(Collectors.toList());
        return response;
    }

    private List<Author> getChatters(List<String> chaterIds, Map<String, Author> authorCache){
        List<Author> chatters = new ArrayList<>();

        chaterIds.forEach(chatterId -> {
            Author at = null;
            if(authorCache.containsKey(chatterId)){
                at = authorCache.get(chatterId);
            } else {
                User u = userRepository.findById(chatterId)
                        .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));
                at = modelMapper.map(u, Author.class);
                at.setId(chatterId);
            }
            chatters.add(at);
        });

        return chatters;
    }

    public Map<String, Object> deleteRoom(ObjectId roomId, String meId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.ROOM_NOT_FOUND));
        if(!room.getUserIds().contains(meId)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }
        roomRepository.delete(room);
        messageRepository.deleteByRoomId(roomId);
        return RespHelper.ok();
    }

    public List<Message> getNewMessage(ObjectId roomId){
        List<Message> messages = messageRepository.findByRoomIdAndRead(roomId, false);
        return messages;
    }

    public List<Message> getListMessage(ObjectId roomId, ObjectId lastMessageId, int size){
        Pageable paging = PageRequest.of(0, size, Sort.by("id").descending());
        List<Message> messages = null;
        if(lastMessageId == null){
            messages = messageRepository.findByRoomId(roomId, paging);
        } else {
            messages = messageRepository.findByRoomIdAndIdLessThan(roomId, lastMessageId, paging);
        }
        return messages;
//        return messageDAO.findByRoomId(roomId, index, size);
    }
}
