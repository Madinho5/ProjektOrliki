package com.example.ProjektOrliki.tournament.repository;

import com.example.ProjektOrliki.tournament.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByName(String name);
}