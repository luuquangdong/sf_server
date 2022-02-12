package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.model.exception.ExceptionType;
import com.it5240.sportfriendfinding.model.unit.Media;
import com.it5240.sportfriendfinding.model.dto.auth.ChangePasswordInfo;
import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserReq;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.model.entity.FriendRequest;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.FriendRequestRepository;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.repository.dao.UserDao;
import com.it5240.sportfriendfinding.service.cosin.similarity.CosineSimilarity;
import com.it5240.sportfriendfinding.utils.RespHelper;
import com.it5240.sportfriendfinding.utils.Uploader;
import com.it5240.sportfriendfinding.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Uploader uploader;
    @Autowired
    private UserUtil userHelper;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private CosineSimilarity cosineSimilarity;

    public UserResp getUser(String id, String meId){
        User user = findById(id);
        UserResp resp = userHelper.toUserResp(user);

        if(id.equals(meId)){
            resp.setCanEdit(true);
            return resp;
        }
        
        if(user.getFriendIds().contains(meId)){
            resp.setFriend(true);
            return resp;
        }

        Optional<FriendRequest> frOpt = friendRequestRepository.findByUserIdSentAndUserIdReceive(meId, id);
        if(frOpt.isPresent()){
            resp.setRequestedFriend(true);
        }

        return resp;
    }

    public Media updateAvatar(MultipartFile avatarFile, String meId){
        User user = userRepository.findById(meId).get();
        if(avatarFile == null || !avatarFile.getContentType().startsWith("image")){
            throw InvalidExceptionFactory.get(ExceptionType.FILE_IS_NOT_IMAGE);
        }
        Media avatar = uploader.uploadImage(avatarFile, "avatar");
        if(user.getAvatar() != null) {
            uploader.deleteFile(user.getAvatar().getId());
        }
        user.setAvatar(avatar);
        userRepository.save(user);
        return avatar;
    }

    public UserResp updateUserInfo(UserReq newUserInfo, String phoneNumber){
        if(!newUserInfo.getPhoneNumber().equals(phoneNumber)){
           throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        User user = findById(phoneNumber);
        user.updateInfo(newUserInfo);

        User userUpdated = userRepository.save(user);

        return userHelper.toUserRespV2(userUpdated, phoneNumber);
    }

    public List<User> getListUser(List<String> userIds){
        List<User> result = userRepository.findByPhoneNumberIn(userIds);
        return result;
    }

    public Map<String, Object> changePassword(ChangePasswordInfo info, String phoneNumber){
        User user = findById(phoneNumber);

        // if(!user.getPassword().equals(passwordEncoder.encode(info.getOldPassword()))){
        //     throw InvalidExceptionFactory.get(ExceptionType.PASSWORD_NOT_MATCH);
        // }

        if(!passwordEncoder.matches(info.getOldPassword(), user.getPassword())){
            throw InvalidExceptionFactory.get(ExceptionType.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(info.getPassword()));

        userRepository.save(user);

        return RespHelper.ok();
    }

//    public List<UserResp> recommendFriends(SearchInfo searchInfo, String meId){
//        User me = userRepository.findById(meId).get();
//
//        Set<String> notIds = me.getFriendIds();
//        notIds.add(meId);
//
//        List<UserResp> result = userDao.recommendFriends(searchInfo, notIds)
//                .stream()
//                .map(user -> userHelper.toUserResp(user))
//                .collect(Collectors.toList());
//        return result;
//    }
    public List<UserResp> suggestFriends(SearchInfo searchInfo, String meId){
        User me = userRepository.findById(meId).get();

        Set<String> notIds = me.getFriendIds();
        notIds.add(meId);

        List<User> users = userDao.suggestFriends(searchInfo, notIds);
        users = cosineSimilarity.sortByCosineSimilarity(users, searchInfo);

        return users.stream()
                .map(user -> userHelper.toUserResp(user))
                .collect(Collectors.toList());
    }

    private User findById(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));
    }
}
