package com.example.ProjektOrliki.player.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.mapper.PlayerMapper;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.player.service.api.PlayerCreator;
import com.example.ProjektOrliki.player.service.api.PlayerDeleter;
import com.example.ProjektOrliki.player.service.api.PlayerModifier;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService implements PlayerCreator, PlayerModifier, PlayerDeleter {

    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final CurrentUserService currentUserService;
    private final PlayerMapper playerMapper;

    private PlayerPosition parsePosition(String pos) {
        try {
            return PlayerPosition.valueOf(pos.toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Nieprawidłowa pozycja");
        }
    }
    private Team getTrainerTeam() {
        User trainer = currentUserService.getCurrentUser();
        return teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));
    }

    @Override
    public PlayerResponse createPlayer(PlayerRequest request) {
        User trainer = currentUserService.getCurrentUser();

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Nie masz przypisanej drużyny."));

        if (team.getPlayers().size() >= 20) {
            throw new IllegalArgumentException("Drużyna osiągnęła limit 20 zawodników.");
        }

        Player player = Player.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .position(parsePosition(request.getPosition()))
                .team(team)
                .build();

        playerRepository.save(player);
        return playerMapper.toResponse(player);
    }

    @Override
    public PlayerResponse updatePlayer(Long id, PlayerRequest request) {

        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zawodnika."));

        if (!player.getTeam().getId().equals(getTrainerTeam().getId())) {
            throw new IllegalArgumentException("Nie możesz edytować zawodnika z innej drużyny.");
        }

        player.setFirstName(request.getFirstName());
        player.setLastName(request.getLastName());
        player.setAge(request.getAge());
        player.setPosition(parsePosition(request.getPosition()));

        playerRepository.save(player);
        return playerMapper.toResponse(player);
    }

    @Override
    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono zawodnika."));

        if (!player.getTeam().getId().equals(getTrainerTeam().getId())) {
            throw new IllegalArgumentException("Nie możesz usuwać zawodników z innej drużyny.");
        }
        playerRepository.delete(player);
    }
}
