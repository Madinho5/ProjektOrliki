package com.example.ProjektOrliki.match.repository;

import com.example.ProjektOrliki.match.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTournamentId(Long tournamentId);
    List<Match> findByTournamentIdAndRoundNumber(Long tournamentId, Integer roundNumber);
    @Modifying
    void deleteByTournamentId(Long tournamentId);
}
