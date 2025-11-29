package com.example.ProjektOrliki.tournament.repository;

import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    boolean existsByName(String name);
    List<Tournament> findByStatus(TournamentStatus status);
}