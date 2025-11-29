package com.example.ProjektOrliki.team.repository;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.team.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByTrainer(User trainer);
    boolean existsByName(String name);
    Optional<Team> findByTrainer(User trainer);
}
