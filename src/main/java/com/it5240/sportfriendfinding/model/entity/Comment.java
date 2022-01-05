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
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Comment {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId id;

    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId postId;

    protected String authorId;

    @NotBlank(message = "Content must have charactors")
    protected String content;

    @CreatedDate
    protected LocalDateTime createdTime;
}
