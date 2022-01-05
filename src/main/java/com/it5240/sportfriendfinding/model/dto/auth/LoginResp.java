package com.it5240.sportfriendfinding.model.dto.auth;

import com.it5240.sportfriendfinding.model.dto.user.UserResp;
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
