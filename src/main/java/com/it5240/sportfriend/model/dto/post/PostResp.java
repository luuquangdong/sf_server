package com.it5240.sportfriend.model.dto.post;

import com.it5240.sportfriend.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResp extends Post {
    private Author author;
    private boolean canEdit;
    private boolean isLiked;
    private int likeCount;
}
