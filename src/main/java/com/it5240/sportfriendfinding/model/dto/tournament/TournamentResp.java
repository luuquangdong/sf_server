package com.it5240.sportfriendfinding.model.dto.tournament;

import com.it5240.sportfriendfinding.model.entity.Tournament;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResp extends Tournament {
    private boolean joined;
    private boolean canEdit;
    private boolean requested;
}
