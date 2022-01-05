package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.model.dto.chat.RoomResp;
import com.it5240.sportfriendfinding.model.entity.Message;
import com.it5240.sportfriendfinding.model.entity.Room;
import com.it5240.sportfriendfinding.repository.MessageRepository;
import com.it5240.sportfriendfinding.repository.RoomRepository;
import com.it5240.sportfriendfinding.repository.dao.MessageDao;
import com.it5240.sportfriendfinding.utils.RespHelper;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
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

    public Room createRoom(Room newRoom){
        newRoom.setId(null);
        newRoom = roomRepository.save(newRoom);
        return newRoom;
    }

    public Message createMessage(Message message){
        Room room = roomRepository.findById(message.getRoomId())
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.ROOM_NOT_FOUND));

        message = messageRepository.save(message);
        roomRepository.save(room);

        return message;
    }

    public List<RoomResp> getListRoom(String userId){
        List<Room> rooms = roomRepository.findByUserIds(userId);
        List<RoomResp> response = rooms.stream()
                .map(room -> {
                    RoomResp roomResponse = modelMapper.map(room, RoomResp.class);

//                    List<Message> messages = messageRepository.findByRoomId(room.getId());
                    List<Message> messageList = messageDAO.findByRoomId(room.getId(), 0, 20);
                    roomResponse.setMessages(messageList);

                    return roomResponse;
                })
                .sorted(Comparator.comparing(Room::getLastModified).reversed())
                .collect(Collectors.toList());
        return response;
    }

    public String deleteRoom(ObjectId roomId, String meId){
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
