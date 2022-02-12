package com.it5240.sportfriendfinding.model.dto.tournament;

import com.it5240.sportfriendfinding.model.dto.post.Author;
import com.it5240.sportfriendfinding.model.entity.TournamentPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPostResp extends TournamentPost {
    private Author author;
    private boolean canEdit;
    private boolean isLiked;
    private int likeCount;
}
