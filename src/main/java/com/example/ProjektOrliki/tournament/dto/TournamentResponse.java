package com.example.ProjektOrliki.tournament.dto;

import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.ProjektOrliki.tournament.model.Tournament}
 */
@Value
public class TournamentResponse implements Serializable {
    String name;
    LocalDate startDate;
    TournamentStatus status;
    Integer teamCount;
}