package com.example.ProjektOrliki.player.service;

import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public Player createPlayer(Long trainerId, String firstName,
                               String lastName, Integer age, String position) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() ->
                        new RuntimeException("Trainer with id" + trainerId + " not found"));
        if (!trainer.getRole().equals(Role.TRAINER)) {
            throw new RuntimeException("User with id " + trainerId + " is not a trainer");
        }

            Player player = Player.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .age(age)
                    .position(position)
                    .trainer(trainer)
                    .build();
            return playerRepository.save(player);
    }

    public List<Player> getPlayersByTrainer(Long trainerId) {
        return playerRepository.findByTrainerId(trainerId);
    }
}
