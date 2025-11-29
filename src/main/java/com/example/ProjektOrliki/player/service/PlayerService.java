package com.example.ProjektOrliki.player.service;

import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.InvalidIsolationLevelException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

//    public Player createPlayer(Long trainerId, String firstName,
//                               String lastName, Integer age, PlayerPosition position) {
//        User trainer = userRepository.findById(trainerId)
//                .orElseThrow(() ->
//                        new IllegalArgumentException("Trainer with id " + trainerId + " not found"));
//        if (!trainer.getRole().equals(Role.TRAINER)) {
//            throw new IllegalArgumentException("User with id " + trainerId + " is not a trainer");
//        }
//
//            Player player = Player.builder()
//                    .firstName(firstName)
//                    .lastName(lastName)
//                    .age(age)
//                    .position(position)
//                    .team(team)
//                    .build();
//            return playerRepository.save(player);
//    }
//
//    public List<Player> getPlayersByTeam(Long trainerId) {
//        return playerRepository.findByTrainerId(trainerId);
//    }
}
