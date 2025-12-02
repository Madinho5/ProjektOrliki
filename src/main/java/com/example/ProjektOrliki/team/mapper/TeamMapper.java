package com.example.ProjektOrliki.team.mapper;

import com.example.ProjektOrliki.team.dto.TeamDetailsResponse;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.team.model.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getTrainer().getFirstName() + " " + team.getTrainer().getLastName()
        );
    }

    public TeamDetailsResponse toDetailsResponse(Team team) {
        var players = team.getPlayers().stream().map(p -> new TeamDetailsResponse.PlayerDto(
                p.getId(),
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getPosition()
        )).toList();

        return new TeamDetailsResponse(
                team.getId(),
                team.getName(),
                players
        );
    }
}
