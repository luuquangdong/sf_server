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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private String id;

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId roomId;

    @Pattern(regexp = "^0\\d{9}$", message = "senderId invalid")
    private String senderId;

    @Pattern(regexp = "^0\\d{9}$", message = "receiveId invalid")
    private String receiveId;

    @NotBlank(message = "Content can't blank")
    private String content;

    private boolean read;

    @CreatedDate
    private LocalDateTime createdTime;
}
