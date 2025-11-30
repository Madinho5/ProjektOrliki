package com.example.ProjektOrliki.team.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.team.dto.TeamDetailsResponse;
import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;

    private TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getTrainer().getFirstName() + " " + team.getTrainer().getLastName()
        );
    }

    private TeamDetailsResponse toDetailsResponse(Team team) {
        List<TeamDetailsResponse.PlayerDto> players = team.getPlayers().stream()
                .map(p -> new TeamDetailsResponse.PlayerDto(
                        p.getId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getAge(),
                        p.getPosition()
                ))
                .toList();

        return new TeamDetailsResponse(
                team.getId(),
                team.getName(),
                players
        );
    }

    public TeamResponse createTeam(TeamRequest request) {
        User trainer = currentUserService.getCurrentUser();

        if (teamRepository.existsByTrainer(trainer)) {
            throw new IllegalArgumentException(("Ten trener ma już przypisaną drużynę."));
        }

        if (teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Drużyna o takiej nazwie już istnieje.");
        }

        Team team = Team.builder()
                .name(request.getName())
                .trainer(trainer)
                .build();
        return toResponse(teamRepository.save(team));
    }

    public TeamDetailsResponse getMyTeam() {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));
        return toDetailsResponse(team);
    }

    public TeamResponse updateMyTeam(TeamRequest request) {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        if (!team.getName().equals(request.getName()) &&
                teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Drużyna o takiej nazwie już istnieje.");
        }

        team.setName(request.getName());
        teamRepository.save(team);

        return toResponse(team);
    }

    public void deleteMyTeam() {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        teamRepository.delete(team);
    }
}
