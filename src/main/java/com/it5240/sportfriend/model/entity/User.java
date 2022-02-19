package com.it5240.sportfriend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.it5240.sportfriend.model.dto.user.UserReq;
import com.it5240.sportfriend.model.unit.Role;
import com.it5240.sportfriend.model.unit.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends UserBase {
    //    @Id
//    @JsonSerialize(using = ToStringSerializer.class)
//    private ObjectId id;
    @JsonIgnore
    @NotBlank(message = "password is required")
    @Size(min=8, message = "password must be at least 8 charactor")
    private String password;

    @JsonIgnore
    private List<ObjectId> sportIds;

    @NotBlank(message = "role is required")
    protected Role role;

    protected boolean isVerifiedPhoneNumber;
    @CreatedDate
    protected LocalDateTime createdTime;
    @LastModifiedDate
    protected LocalDateTime lastModified;

    protected Set<String> friendIds;

    protected boolean banned;

    public void updateInfo(UserReq newUserInfo){
        this.name = newUserInfo.getName();
        this.birthday = newUserInfo.getBirthday();
        this.sportIds = newUserInfo.getSportIds();
        this.location = newUserInfo.getLocation();
        // this.avatar = newUserInfo.getAvatar();
        this.gender = newUserInfo.getGender();
        this.description = newUserInfo.getDescription();
        this.updatedInfo = true;
    }

}