package com.it5240.sportfriend.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.it5240.sportfriend.model.unit.Media;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupOrganization {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    @NotNull
    private Media idCard;
    @NotBlank
    private String userId;
    @CreatedDate
    private LocalDateTime createdTime;
}
