package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.exception.NotFoundExceptionFactory;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.dto.user.ShortInfoUser;
import com.it5240.sportfriend.model.entity.FriendRequest;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.FriendRequestRepository;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.utils.NotificationUtil;
import com.it5240.sportfriend.utils.RespHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private NotificationUtil notificationUtil;

    public Map<String, Object> requestMakeFriend(FriendRequest friendRequest, String meId){
        if(!meId.equals(friendRequest.getUserIdSent())){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }
        userRepository.findById(friendRequest.getUserIdReceive())
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.USER_NOT_FOUND)
                );

        Optional<FriendRequest> frOpt =  friendRequestRepository.findByUserIdSentAndUserIdReceive(friendRequest.getUserIdSent(), friendRequest.getUserIdReceive());
        boolean result = false;
        if(frOpt.isPresent()){
            friendRequestRepository.delete(frOpt.get());
        } else {
            friendRequestRepository.save(friendRequest);
            result = true;
        }

        return Map.of("requested", result);
    }

    public List<ShortInfoUser> getListFriendRequest(String meId){
        List<String> userIds = friendRequestRepository.findByUserIdReceive(meId)
                .stream()
                .map(friendRequest -> friendRequest.getUserIdSent())
                .collect(Collectors.toList());
        List<ShortInfoUser> result = userRepository.findByPhoneNumberIn((userIds))
                .stream()
                .map(user -> mapper.map(user, ShortInfoUser.class))
                .collect(Collectors.toList());
        return result;
    }

    public Map<String, Object> answerRequest(String meId, String requesterId, boolean answer){
        FriendRequest friendRequest = friendRequestRepository.findByUserIdSentAndUserIdReceive(requesterId, meId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.FRIEND_REQUEST_NOT_FOUND)
                );
        User me = userRepository.findById(meId).get();
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.USER_NOT_FOUND)
                );
        if(answer){
            me.getFriendIds().add(requesterId);
            requester.getFriendIds().add(meId);
            userRepository.saveAll(List.of(me, requester));

            notificationUtil.sendNotificationToUser(requester, "Thông báo", String.format("Bạn và %s đã trở thành bạn bè", me.getName()));
        } else {
            notificationUtil.sendNotificationToUser(requester, "Thông báo", String.format("%s đã từ chối lời mời kết bạn của bạn", me.getName()));
        }
        friendRequestRepository.delete(friendRequest);
        return RespHelper.ok();
    }

    public List<ShortInfoUser> getListFriend(String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory
                        .get(ExceptionType.USER_NOT_FOUND)
                );
        return userRepository.findByPhoneNumberIn(
                        new ArrayList<>(user.getFriendIds())
                )
                .stream()
                .map(u -> mapper.map(u, ShortInfoUser.class))
                .collect(Collectors.toList());
    }

    public Map<String, Object> deleteFriend(String meId, String friendId){
        User me = userRepository.findById(meId).get();
        User user = userRepository.findById(friendId).get();

        me.getFriendIds().remove(friendId);
        user.getFriendIds().remove(meId);

        userRepository.save(me);
        userRepository.save(user);

        return RespHelper.ok();
    }
}
