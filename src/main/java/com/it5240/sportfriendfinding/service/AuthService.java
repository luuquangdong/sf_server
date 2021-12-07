package com.it5240.sportfriendfinding.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.it5240.sportfriendfinding.dto.LoginRequest;
import com.it5240.sportfriendfinding.entity.User;
import com.it5240.sportfriendfinding.repository.UserRepository;
import com.it5240.sportfriendfinding.security.JwtProperties;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    public String login(LoginRequest loginInfo){
        String token = authenticate(loginInfo);
        String role = userRepository.findById(loginInfo.getPhoneNumber()).get().getRole();

        if(!"ROLE_USER".equals(role) && !"ROLE_ORGANIZATION".equals(role)){
            throw new AccessDeniedException("You have no role to access it");
        }
        return token;
    }

    public String adminLogin(LoginRequest loginInfo){
        String result = authenticate(loginInfo);
        User user = userRepository.findById(loginInfo.getPhoneNumber()).get();
        if(!"ROLE_ADMIN".equals(user.getRole())){
            throw new AccessDeniedException("You have no role to access it");
        }
        return result;
    }

    private String authenticate(LoginRequest loginInfo){
        String phoneNumber = loginInfo.getPhoneNumber();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, loginInfo.getPassword(), new ArrayList<>()));
        String token = generateToken(phoneNumber);
        return token;
    }

    private String generateToken(String phoneNumber){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JwtProperties.EXPIRATION);

        String token = JWT.create()
                .withSubject(phoneNumber)
                .withIssuedAt(now)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        return token;
    }
}
