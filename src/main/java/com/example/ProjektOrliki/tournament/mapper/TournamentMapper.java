package com.example.ProjektOrliki.tournament.mapper;

import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.Tournament;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public TournamentResponse toResponse(Tournament t){
        return new TournamentResponse(
                t.getId(),
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount()
        );
    }

    public TournamentDetailsResponse toDetailsResponse(Tournament t){
        var teams = t.getTeams().stream()
                .map(team -> new TournamentDetailsResponse.TeamDto(team.getId(), team.getName()))
                .toList();

        Long winnerId = t.getWinner().getId();
        String winnerName = t.getWinner().getName();

        return new TournamentDetailsResponse(
                t.getId(),
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount(),
                teams,
                winnerId,
                winnerName
        );
    }
}
