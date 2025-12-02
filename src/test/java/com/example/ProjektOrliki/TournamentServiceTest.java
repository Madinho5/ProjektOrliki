package com.example.ProjektOrliki;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.mapper.TournamentMapper;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import com.example.ProjektOrliki.tournament.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class TournamentServiceTest {

    private TournamentService service;
    private TournamentRepository tournamentRepository;
    private TeamRepository teamRepository;
    private CurrentUserService currentUserService;
    private MatchRepository matchRepository;

    private Tournament tournament;

    @BeforeEach
    void setUp() {
        tournamentRepository = mock(TournamentRepository.class);
        teamRepository = mock(TeamRepository.class);
        currentUserService = mock(CurrentUserService.class);
        matchRepository = mock(MatchRepository.class);
        TournamentMapper mapper = new TournamentMapper();

        service = new TournamentService(tournamentRepository, teamRepository, currentUserService, mapper, matchRepository);

        tournament = Tournament.builder()
                .id(1L)
                .name("Test Cup")
                .status(TournamentStatus.CREATED)
                .teamCount(4)
                .teams(new ArrayList<>())
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    void given_existingName_when_createTournament_then_throwException() {
        TournamentRequest req = new TournamentRequest("ABC", LocalDate.now(), 4);
        when(tournamentRepository.existsByName("ABC")).thenReturn(true);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Turniej o takiej nazwie juz istnieje");
    }

    @Test
    void given_invalidTeamCount_when_createTournament_then_throwException() {
        TournamentRequest req = new TournamentRequest("ABC", LocalDate.now(), 3);

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Liczba drużyn musi być jedną z: 2, 4, 8 lub 16.");
    }

    @Test
    void given_existingTournament_when_getById_then_returnDto() {
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));

        TournamentDetailsResponse response = service.getById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getWinnerId()).isNull();
    }

    @Test
    void given_missingTournament_when_getById_then_throwException() {
        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nie znaleziono turnieju o id: 99");
    }

    @Test
    void given_existingTournament_when_delete_then_deleted() {
        when(tournamentRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(matchRepository).deleteByTournamentId(1L);
        verify(tournamentRepository).deleteById(1L);
    }

    @Test
    void given_notExistingTournament_when_delete_then_throwException() {
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nie znaleziono turnieju o id: 99");
    }

    @Test
    void given_teamWithoutGoalkeeper_when_registerTeam_then_throwException() {
        var user = new User();

        Team team = new Team();
        team.setPlayers(List.of(
                player(PlayerPosition.DEF),
                player(PlayerPosition.DEF),
                player(PlayerPosition.DEF),
                player(PlayerPosition.MID),
                player(PlayerPosition.MID),
                player(PlayerPosition.ST),
                player(PlayerPosition.ST)
        ));

        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(teamRepository.findByTrainer(user)).thenReturn(Optional.of(team));

        tournament.setStatus(TournamentStatus.REGISTRATION_OPENED);

        assertThatThrownBy(() -> service.registerTeam(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Drużyna musi mieć przynajmniej jednego bramkarza.");
    }

    private Player player(PlayerPosition pos) {
        Player p = new Player();
        p.setPosition(pos);
        return p;
    }
}
