package com.it5240.sportfriendfinding.service;

import com.it5240.sportfriendfinding.exception.InvalidExceptionFactory;
import com.it5240.sportfriendfinding.model.exception.AuthException;
import com.it5240.sportfriendfinding.model.exception.ExceptionType;
import com.it5240.sportfriendfinding.model.unit.Role;
import com.it5240.sportfriendfinding.model.dto.auth.AuthInfo;
import com.it5240.sportfriendfinding.model.dto.auth.LoginResp;
import com.it5240.sportfriendfinding.model.dto.auth.SignUpInfo;
import com.it5240.sportfriendfinding.model.dto.user.UserResp;
import com.it5240.sportfriendfinding.model.entity.User;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.utils.JwtUtil;
import com.it5240.sportfriendfinding.utils.RespHelper;
import com.it5240.sportfriendfinding.utils.UserUtil;
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
        String token = authenticate(loginInfo);
        User user = userRepository.findById(loginInfo.getPhoneNumber()).get();

        // if(!Role.ROLE_USER.equals(user.getRole()) && !Role.ROLE_ORGANIZATION.equals(user.getRole())){
        //     throw new AccessDeniedException("You have no role to access it");
        // }
        UserResp userDto = userHelper.toUserResp(user);
        return new LoginResp(userDto, token);
    }

    public LoginResp adminLogin(AuthInfo loginInfo){
        String token = authenticate(loginInfo);
        User user = userRepository.findById(loginInfo.getPhoneNumber()).get();
        if(!Role.ROLE_ADMIN.equals(user.getRole())){
            throw new AccessDeniedException("You have no role to access it");
        }
        UserResp userDto = userHelper.toUserResp(user);
        return new LoginResp(userDto, token);
    }

    private String authenticate(AuthInfo loginInfo){
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
        String token = jwtUtil.generateToken(phoneNumber);
        return token;
    }

}
