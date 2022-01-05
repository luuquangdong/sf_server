package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.model.dto.user.FriendResp;
import com.it5240.sportfriendfinding.model.entity.FriendRequest;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.FriendRequestRepository;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.utils.RespHelper;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    public String createFriendRequest(FriendRequest friendRequest){
        userRepository.findById(friendRequest.getUserIdReceive())
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.USER_NOT_FOUND)
                );

        friendRequest.setId(null);
        friendRequestRepository.save(friendRequest);

        return RespHelper.ok();
    }

    public List<FriendRequest> getListFriendRequest(String meId){
        return friendRequestRepository.findByUserIdReceive(meId);
    }

    public String answerRequest(String meId, ObjectId requestId, boolean answer){
        FriendRequest friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.FRIEND_REQUEST_NOT_FOUND)
                );
        User me = userRepository.findById(meId).get();
        if(answer){
            String friendId = friendRequest.getUserIdSent();
            me.getFriendIds().add(friendId);
            userRepository.save(me);
        }
        friendRequestRepository.delete(friendRequest);
        return RespHelper.ok();
    }

    public List<FriendResp> getListFriend(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.USER_NOT_FOUND)
                );
        return userRepository.findByPhoneNumberIn(
                        new ArrayList<>(user.getFriendIds())
                )
                .stream()
                .map(u -> mapper.map(u, FriendResp.class))
                .collect(Collectors.toList());
    }

    public String deleteFriend(String meId, String friendId){
        User me = userRepository.findById(meId).get();
        me.getFriendIds().remove(friendId);
        return RespHelper.ok();
    }
}
