package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.match.model.Match;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BracketService {

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final Random random = new Random();

    public List<MatchDto> generateNextRound(Long tournamentId) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju: " + tournamentId));

        if (tournament.getStatus() == TournamentStatus.FINISHED) {
            throw new IllegalStateException("Turniej został już zakończony.");
        }

        if (tournament.getStatus() != TournamentStatus.IN_PROGRESS &&
                tournament.getStatus() != TournamentStatus.REGISTRATION_CLOSED) {
            throw new IllegalStateException("Turniej nie jest gotowy do generowania rund.");
        }

        if (tournament.getCurrentRound() == null) {
            tournament.setCurrentRound(1);
        }

        int round = tournament.getCurrentRound();
        List<Team> teams;

        if (round == 1) {
            teams = new ArrayList<>(tournament.getTeams());
            Collections.shuffle(teams);

        } else {
            int prevRound = round - 1;

            List<Match> prevMatches =
                    matchRepository.findByTournamentIdAndRoundNumber(tournamentId, prevRound);

            if (prevMatches.isEmpty()) {
                throw new IllegalStateException("Poprzednia runda nie została jeszcze rozegrana.");
            }

            teams = prevMatches.stream()
                    .map(Match::getWinner)
                    .collect(Collectors.toCollection(ArrayList::new));

        }

        List<MatchDto> dtos = new ArrayList<>();
        List<Team> nextRoundTeams = new ArrayList<>();

        int matchNumber = 1;

        for (int i = 0; i < teams.size(); i += 2) {

            Team a = teams.get(i);
            Team b = teams.get(i + 1);

            int scoreA = random.nextInt(6);
            int scoreB = random.nextInt(6);

            Team winner = (scoreA >= scoreB) ? a : b;

            Match match = Match.builder()
                    .tournament(tournament)
                    .roundNumber(round)
                    .matchNumber(matchNumber++)
                    .teamA(a)
                    .teamB(b)
                    .scoreA(scoreA)
                    .scoreB(scoreB)
                    .winner(winner)
                    .build();

            matchRepository.save(match);

            nextRoundTeams.add(winner);

            dtos.add(new MatchDto(
                    match.getId(),
                    match.getMatchNumber(),
                    a.getName(),
                    b.getName(),
                    scoreA,
                    scoreB,
                    winner.getName()
            ));
        }

        if (nextRoundTeams.size() == 1) {
            Team champ = nextRoundTeams.get(0);
            tournament.setWinner(champ);
            tournament.setStatus(TournamentStatus.FINISHED);
            tournamentRepository.save(tournament);
            return dtos;
        }

        tournament.setCurrentRound(round + 1);
        tournament.setStatus(TournamentStatus.IN_PROGRESS);
        tournamentRepository.save(tournament);

        return dtos;
    }




}
