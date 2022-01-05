package com.it5240.sportfriendfinding.model.atom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private int round;
    private LocalDateTime dateTime;
    private String team1;
    private int score1;
    private String team2;
    private int score2;
    private String winner;
}
