package com.it5240.sportfriendfinding.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequest {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Pattern(regexp = "^0\\d{9}$", message = "user Id sent request invalid")
    private String userIdSent;

    @Pattern(regexp = "^0\\d{9}$", message = "user id receive request invalid")
    private String userIdReceive;

    @CreatedDate
    private LocalDateTime createdTime;
}
