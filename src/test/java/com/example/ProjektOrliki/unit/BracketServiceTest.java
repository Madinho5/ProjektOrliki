package com.example.ProjektOrliki.unit;

import com.example.ProjektOrliki.bracket.dto.BracketDto;
import com.example.ProjektOrliki.bracket.service.BracketService;
import com.example.ProjektOrliki.bracket.service.api.BracketRandomizer;
import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.match.model.Match;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

public class BracketServiceTest {
    private BracketService service;
    private TournamentRepository tournamentRepository;
    private MatchRepository matchRepository;
    private BracketRandomizer randomizer;

    private Tournament tournament;
    private Team teamA;
    private Team teamB;

    @BeforeEach
    public void setUp(){
        tournamentRepository = mock(TournamentRepository.class);
        matchRepository = mock(MatchRepository.class);
        randomizer = mock(BracketRandomizer.class);

        service = new BracketService(tournamentRepository,matchRepository, randomizer);

        teamA = new Team();
        teamA.setId(1L);
        teamA.setName("Team A");

        teamB = new Team();
        teamB.setId(2L);
        teamB.setName("Team B");

        tournament = new Tournament();
        tournament.setId(10L);
        tournament.setName("Tournament");
        tournament.setStatus(TournamentStatus.REGISTRATION_CLOSED);
        tournament.setTeams(List.of(teamA,teamB));

        when(tournamentRepository.findById(10L)).thenReturn(Optional.of(tournament));
    }

    @Test
    void given_validTournament_when_generateRound1_then_matchesAreCreated() {
        when(randomizer.generateScore(any()))
                .thenReturn(3, 1);

        List<MatchDto> result = service.generateNextRound(10L);

        assertThat(result).hasSize(1);
        verify(matchRepository, times(1)).save(any(Match.class));
        verify(tournamentRepository, times(1)).save(tournament);
        verify(randomizer, times(2)).generateScore(any());
    }

    @Test
    void given_finishedTournament_when_generateNextRound_then_throwException() {
        tournament.setStatus(TournamentStatus.FINISHED);

        assertThatThrownBy(() -> service.generateNextRound(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Turniej został już zakończony.");
    }

    @Test
    void given_wrongStatus_when_generateNextRound_then_throwException() {

        tournament.setStatus(TournamentStatus.CREATED);

        assertThatThrownBy(() -> service.generateNextRound(10L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Turniej nie jest gotowy do generowania rund.");
    }

    @Test
    void given_noMatches_when_getBracket_then_returnEmptyBracket() {
        when(matchRepository.findByTournamentId(10L)).thenReturn(List.of());

        BracketDto dto = service.getBracket(10L);

        assertThat(dto.getRounds()).isEmpty();
    }

    @Test
    void given_existingMatches_when_getBracket_then_roundsAreGrouped() {
        Match m = Match.builder()
                .id(1L)
                .roundNumber(1)
                .matchNumber(1)
                .teamA(teamA)
                .teamB(teamB)
                .scoreA(2)
                .scoreB(1)
                .winner(teamA)
                .build();

        when(matchRepository.findByTournamentId(10L)).thenReturn(List.of(m));

        BracketDto dto = service.getBracket(10L);

        assertThat(dto.getRounds()).hasSize(1);
        assertThat(dto.getRounds().getFirst().getRoundNumber()).isEqualTo(1);
        assertThat(dto.getRounds().getFirst().getMatches()).hasSize(1);
    }
}
