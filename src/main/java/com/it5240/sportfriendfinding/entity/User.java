package com.it5240.sportfriendfinding.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    //    @Id
//    @JsonSerialize(using = ToStringSerializer.class)
//    private ObjectId id;
    @Id
    @Pattern(regexp = "^0\\d{9}$", message = "phone number invalid")
    private String phoneNumber;

    @NotBlank(message = "password is required")
    @Size(min=8, message = "password must be at least 5 charactor")
    private String password;

    private String fullName;
    private String role;
    private boolean isVerifiedPhoneNumber;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

}