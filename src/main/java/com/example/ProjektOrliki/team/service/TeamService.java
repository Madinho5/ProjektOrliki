package com.example.ProjektOrliki.team.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    public TeamResponse getMyTeam() {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));
        return toResponse(team);
    }
}
