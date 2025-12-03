package com.example.ProjektOrliki.team.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.team.dto.TeamDetailsResponse;
import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.mapper.TeamMapper;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;
    private final TeamMapper teamMapper;

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
        return teamMapper.toResponse(teamRepository.save(team));
    }

    public TeamDetailsResponse getMyTeam() {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));
        return teamMapper.toDetailsResponse(team);
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

        return teamMapper.toResponse(team);
    }

    @Transactional
    public void deleteMyTeam() {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        boolean locked = team.getTournaments().stream()
                .anyMatch(t -> t.getStatus() != TournamentStatus.CREATED
                        && t.getStatus() != TournamentStatus.REGISTRATION_OPENED
                        && t.getStatus() != TournamentStatus.REGISTRATION_CLOSED);

        if (locked) {
            throw new IllegalArgumentException(
                    "Nie można usunąć drużyny, ponieważ bierze udział w turnieju, który już wystartował lub został zakończony."
            );
        }

        team.getTournaments()
                .forEach(t -> t.getTeams().remove(team));
        teamRepository.delete(team);
    }
}
