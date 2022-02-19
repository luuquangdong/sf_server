package com.it5240.sportfriend.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId id;

    @NotNull
    @Size(min=2)
    protected List<String> userIds;

    @LastModifiedDate
    protected LocalDateTime lastModified;
}
