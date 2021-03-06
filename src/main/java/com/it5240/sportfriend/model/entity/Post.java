package com.it5240.sportfriend.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.it5240.sportfriend.model.unit.Media;
import com.it5240.sportfriend.model.unit.Site;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post {
    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    protected ObjectId id;

    protected String authorId;

    protected String content;

    protected Media image;

    protected Media video;

    protected Set<String> userLikedId;

    protected int commentCount;

    @CreatedDate
    protected LocalDateTime createdTime;
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    protected boolean canComment;

    protected boolean banned;

    protected Site site;

    public Post(String authorId, String content, Site site){
        this.authorId = authorId;
        this.content = content;
        this.site = site;
        this.banned = false;
        this.canComment = true;
        userLikedId = new HashSet<>();
        commentCount = 0;
    }
}
