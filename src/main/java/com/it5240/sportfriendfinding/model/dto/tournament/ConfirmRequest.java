package com.it5240.sportfriendfinding.model.dto.tournament;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmRequest {
    private String userId;
    private boolean agree;
    private ObjectId tournamentId;
}
