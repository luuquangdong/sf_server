package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.exception.NotFoundExceptionFactory;
import com.it5240.sportfriendfinding.exception.model.ExceptionType;
import com.it5240.sportfriendfinding.model.dto.auth.AuthInfo;
import com.it5240.sportfriendfinding.model.dto.user.SearchInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserReq;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.repository.dao.UserDao;
import com.it5240.sportfriendfinding.utils.RespHelper;
import com.it5240.sportfriendfinding.utils.Uploader;
import com.it5240.sportfriendfinding.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UserResp getUser(String id){
        User user = findById(id);
        return userHelper.toUserResp(user);
    }

    public UserResp updateUserInfo(UserReq newUserInfo, String phoneNumber){
        if(!newUserInfo.getPhoneNumber().equals(phoneNumber)){
           throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        User user = findById(phoneNumber);
        user.updateInfo(newUserInfo);

        User userUpdated = userRepository.save(user);

        return userHelper.toUserResp(userUpdated);
    }

    public String changePassword(AuthInfo authInfo, String phoneNumber){
        if(!authInfo.getPhoneNumber().equals(phoneNumber)){
            throw InvalidExceptionFactory.get(ExceptionType.UNAUTHORIZED);
        }

        User user = findById(phoneNumber);
        user.setPassword(passwordEncoder.encode(authInfo.getPassword()));

        userRepository.save(user);

        return RespHelper.ok();
    }

    public List<UserResp> findFriends(SearchInfo searchInfo, String meId){
        User me = userRepository.findById(meId).get();
        List<UserResp> result = userDao.findFriends(searchInfo)
                .stream()
                .map(user -> userHelper.toUserResp(user))
                .collect(Collectors.toList());
        return result;
    }

    private User findById(String userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> NotFoundExceptionFactory.get(ExceptionType.USER_NOT_FOUND));
    }
}
