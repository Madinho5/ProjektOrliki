package com.example.ProjektOrliki.player.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.team.dto.TeamResponse;
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
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika."));
    }

    private Team getTrainerTeam() {
        User trainer = getCurrentUser();
        return teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));
    }
    public PlayerResponse createPlayer(PlayerRequest request) {
        User trainer = getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        if (team.getPlayers().size() >= 20) {
            throw new IllegalArgumentException("Drużyna osiągnęła limit 20 zawodników.");
        }

        Player player = Player.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .position(PlayerPosition.valueOf(request.getPosition()))
                .team(team)
                .build();

        playerRepository.save(player);
        return toResponse(player);
    }

    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {
        Team team = getTrainerTeam();

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zawodnika."));

        if (!player.getTeam().getId().equals(getTrainerTeam().getId())) {
            throw new IllegalArgumentException("Nie możesz edytować zawodnika z innej drużyny.");
        }

        player.setFirstName(request.getFirstName());
        player.setLastName(request.getLastName());
        player.setAge(request.getAge());
        player.setPosition(PlayerPosition.valueOf(request.getPosition()));

        playerRepository.save(player);
        return toResponse(player);
    }

    private PlayerResponse toResponse(Player p) {

        return new PlayerResponse(
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getPosition(),
                p.getTeam().getName(),
                p.getTeam().getTrainer().getFirstName() + " " + p.getTeam().getTrainer().getLastName()
        );
    }

    public void deletePlayer(Long id) {
        Team trainerTeam = getTrainerTeam();

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zawodnika."));

        if (!player.getTeam().getId().equals(trainerTeam.getId())) {
            throw new IllegalArgumentException("Nie możesz usuwać zawodników z innej drużyny.");
        }

        playerRepository.delete(player);
    }


}
