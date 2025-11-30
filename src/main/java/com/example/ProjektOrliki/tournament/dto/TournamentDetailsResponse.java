package com.example.ProjektOrliki.tournament.dto;

import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.example.ProjektOrliki.tournament.model.Tournament}
 */
@Value
public class TournamentDetailsResponse implements Serializable {
    Long id;
    String name;
    LocalDate startDate;
    TournamentStatus status;
    Integer teamCount;
    List<TeamDto> teams;
    Long winnerId;
    String winnerName;

    /**
     * DTO for {@link com.example.ProjektOrliki.team.model.Team}
     */
    @Value
    public static class TeamDto implements Serializable {
        Long id;
        String name;
    }
}