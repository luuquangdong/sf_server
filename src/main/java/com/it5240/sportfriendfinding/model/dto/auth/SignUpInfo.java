package com.it5240.sportfriendfinding.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpInfo {
    @Pattern(regexp = "^0\\d{9}$", message = "phone number invalid")
    private String phoneNumber;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "password is required")
    @Size(min=8, message = "password must be at least 8 charactor")
    private String password;
}
