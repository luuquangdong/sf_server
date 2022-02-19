package com.it5240.sportfriend.model.dto.post;

import com.it5240.sportfriend.model.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResp extends Comment {
    private Author author;
    private boolean canEdit;
}
