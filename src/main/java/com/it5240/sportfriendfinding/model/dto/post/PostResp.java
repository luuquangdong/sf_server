package com.it5240.sportfriendfinding.model.dto.post;

import com.it5240.sportfriendfinding.model.entity.Post;
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
