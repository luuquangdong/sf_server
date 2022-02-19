package com.it5240.sportfriend.model.entity;

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
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ReportPost {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @NotBlank
    private String userId;

    @NotNull
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId postId;

    @NotBlank
    private String reason;

    @CreatedDate
    protected LocalDateTime createdTime;
}
