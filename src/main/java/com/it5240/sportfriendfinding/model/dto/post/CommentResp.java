package com.it5240.sportfriendfinding.model.dto.post;

import com.it5240.sportfriendfinding.model.entity.Comment;
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
