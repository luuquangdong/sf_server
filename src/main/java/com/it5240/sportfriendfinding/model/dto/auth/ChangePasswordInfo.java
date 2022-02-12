package com.it5240.sportfriendfinding.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordInfo {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String password;
}
