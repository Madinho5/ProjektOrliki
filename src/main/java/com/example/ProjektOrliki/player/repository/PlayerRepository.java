package com.example.ProjektOrliki.player.repository;

import com.example.ProjektOrliki.player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByPosition(String position);
    List<Player> findByTrainerId(Long trainerId);
}
