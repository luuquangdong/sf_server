package com.it5240.sportfriend.service;

import com.it5240.sportfriend.exception.InvalidExceptionFactory;
import com.it5240.sportfriend.model.exception.AuthException;
import com.it5240.sportfriend.model.exception.ExceptionType;
import com.it5240.sportfriend.model.unit.Role;
import com.it5240.sportfriend.model.dto.auth.AuthInfo;
import com.it5240.sportfriend.model.dto.auth.LoginResp;
import com.it5240.sportfriend.model.dto.auth.SignUpInfo;
import com.it5240.sportfriend.model.dto.user.UserResp;
import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.utils.JwtUtil;
import com.it5240.sportfriend.utils.RespHelper;
import com.it5240.sportfriend.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserUtil userHelper;
    @Autowired
    private JwtUtil jwtUtil;

    public Map<String, Object> signUp(SignUpInfo signUpInfo){
        Optional<User> userOptional = userRepository.findById(signUpInfo.getPhoneNumber());
        if(userOptional.isPresent()){
            throw InvalidExceptionFactory.get(ExceptionType.PHONE_NUMBER_EXISTED);
        }

        User newUser = userHelper.signUpInfo2User(signUpInfo);
        userRepository.save(newUser);
        return RespHelper.ok();
    }

    public LoginResp login(AuthInfo loginInfo){
        authenticate(loginInfo);
        User user = userRepository.findById(loginInfo.getPhoneNumber()).get();
        String token = jwtUtil.generateToken(user);
        // if(!Role.ROLE_USER.equals(user.getRole()) && !Role.ROLE_ORGANIZATION.equals(user.getRole())){
        //     throw new AccessDeniedException("You have no role to access it");
        // }
        UserResp userDto = userHelper.toUserResp(user);
        return new LoginResp(userDto, token);
    }

    public LoginResp adminLogin(AuthInfo loginInfo){
        authenticate(loginInfo);
        User user = userRepository.findById(loginInfo.getPhoneNumber()).get();
        if(!Role.ROLE_ADMIN.equals(user.getRole())){
            throw new AccessDeniedException("You have no role to access it");
        }
        String token = jwtUtil.generateToken(user);
        UserResp userDto = userHelper.toUserResp(user);
        return new LoginResp(userDto, token);
    }

    private void authenticate(AuthInfo loginInfo){
        String phoneNumber = loginInfo.getPhoneNumber();
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    phoneNumber, 
                    loginInfo.getPassword(), 
                    new ArrayList<>()
                )
            );
        } catch (AuthenticationException ex){
            throw new AuthException();
        }
    }

}
