package com.example.ProjektOrliki.match.repository;

import com.example.ProjektOrliki.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findByTournamentIdAndRoundNumber(Long tournamentId, Integer roundNumber);
}
