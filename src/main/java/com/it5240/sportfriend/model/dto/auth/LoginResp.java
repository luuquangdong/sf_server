package com.it5240.sportfriend.model.dto.auth;

import com.it5240.sportfriend.model.dto.user.UserResp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResp {
    private UserResp user;
    private String token;
}
