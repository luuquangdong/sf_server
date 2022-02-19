package com.it5240.sportfriend.security;

import com.it5240.sportfriend.model.entity.User;
import com.it5240.sportfriend.repository.UserRepository;
import com.it5240.sportfriend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public JwtAuthFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader(JwtUtil.AUTH_HEADER);
        if(authHeader != null && authHeader.startsWith(JwtUtil.TOKEN_PREFIX)){
            Authentication auth = getUsernamePasswordAuthentication(authHeader);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(String header){
        String token = header.replace(JwtUtil.TOKEN_PREFIX, "");
        String userId = null;
        try{
            userId = jwtUtil.getSubjectFromToken(token);
        } catch (Exception e){}
        if(userId == null) return null;

        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isEmpty()) return null;

        CustomUserDetail principal = new CustomUserDetail(userOpt.get());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userId, null, principal.getAuthorities());
        return auth;
    }
}
