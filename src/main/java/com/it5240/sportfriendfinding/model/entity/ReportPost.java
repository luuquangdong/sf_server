package com.it5240.sportfriendfinding.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ReportPost {
    @Id
    private ObjectId id;

    @NotBlank
    private String userId;

    @NotNull
    private ObjectId postId;

    @NotBlank
    private String reason;
}
