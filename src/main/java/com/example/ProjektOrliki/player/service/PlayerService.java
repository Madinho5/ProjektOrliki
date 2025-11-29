package com.example.ProjektOrliki.player.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final CurrentUserService currentUserService;

    public Player createPlayer(PlayerRequest request) {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        if (team.getPlayers().size() >= 10) {
            throw new IllegalArgumentException("Drużyna osiągnęła limit 10 zawodników.");
        }

        Player player = Player.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .position(PlayerPosition.valueOf(request.getPosition()))
                .team(team)
                .build();

        return playerRepository.save(player);
    }
}
