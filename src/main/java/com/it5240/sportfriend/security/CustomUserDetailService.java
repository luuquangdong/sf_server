package com.it5240.sportfriend.security;

import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findById(phoneNumber);
        userOpt.orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
        return new CustomUserDetail(userOpt.get());
    }
}
