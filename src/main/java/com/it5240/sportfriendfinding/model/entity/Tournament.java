package com.it5240.sportfriendfinding.model.entity;

import com.it5240.sportfriendfinding.model.unit.Media;
import com.it5240.sportfriendfinding.model.unit.Schedule;
import com.it5240.sportfriendfinding.model.unit.TournamentBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Tournament extends TournamentBase {
    protected Set<String> participantIds;
    protected Media banner;
    protected List<Schedule> schedules;
    protected Set<String> requesterIds;
}
