package com.it5240.sportfriendfinding.utils;

import com.it5240.sportfriendfinding.model.dto.auth.SignUpInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.model.entity.Sport;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.model.atom.Role;
import com.it5240.sportfriendfinding.repository.SportRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class UserUtil {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper mapper;

    public UserResp toUserResp(User user){
        UserResp userResp = mapper.map(user, UserResp.class);

        if(user.getSportIds() != null){
            List<Sport> sports = sportRepository.findByIdIn(user.getSportIds());
            userResp.setSports(sports);
        }

        return userResp;
    }

    public User signUpInfo2User(SignUpInfo signUpInfo){
        User user = mapper.map(signUpInfo, User.class);
        user.setPassword(passwordEncoder.encode(signUpInfo.getPassword()));
        user.setSportIds(new ArrayList<>());
        user.setFriendIds(new HashSet<>());
        user.setRole(Role.ROLE_USER);
        user.setVerifiedPhoneNumber(false);
        user.setCreatedTime(LocalDateTime.now());

        return user;
    }
}