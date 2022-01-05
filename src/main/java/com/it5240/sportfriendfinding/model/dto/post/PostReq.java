package com.it5240.sportfriendfinding.model.dto.post;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostReq {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Pattern(regexp = "^0\\d{9}$", message = "phone number invalid")
    private String authorId;

    private String content;

    private MultipartFile image;

    private MultipartFile video;
}