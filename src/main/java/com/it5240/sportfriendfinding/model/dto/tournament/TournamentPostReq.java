package com.it5240.sportfriendfinding.model.dto.tournament;

import com.it5240.sportfriendfinding.model.dto.post.PostReq;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TournamentPostReq extends PostReq {
    private ObjectId tournamentId;
}
