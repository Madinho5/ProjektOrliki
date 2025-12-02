package com.example.ProjektOrliki;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.mapper.PlayerMapper;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.player.service.PlayerService;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    private PlayerService service;
    private PlayerRepository playerRepository;

    private Team team;

    @BeforeEach
    void setUp() {
        TeamRepository teamRepository = mock(TeamRepository.class);
        playerRepository = mock(PlayerRepository.class);
        CurrentUserService currentUserService = mock(CurrentUserService.class);
        service = new PlayerService(teamRepository, playerRepository, currentUserService, new PlayerMapper());

        User trainer = new User();
        trainer.setId(1L);

        team = new Team();
        team.setId(10L);
        team.setTrainer(trainer);
        team.setPlayers(Collections.emptyList());

        when(currentUserService.getCurrentUser()).thenReturn(trainer);
        when(teamRepository.findByTrainer(trainer)).thenReturn(Optional.of(team));
    }

    @Test
    void given_validRequest_when_createPlayer_then_playerIsSaved() {

        PlayerRequest request = new PlayerRequest("Jan", "Nowak", 20, "GK");

        service.createPlayer(request);

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(playerRepository).save(captor.capture());
        assertThat(captor.getValue().getPosition()).isEqualTo(PlayerPosition.GK);
    }

    @Test
    void given_invalidPosition_when_createPlayer_then_throwIllegalArgumentException() {

        PlayerRequest request = new PlayerRequest("Jan", "Nowak", 20, "XD");

        assertThatThrownBy(() -> service.createPlayer(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nieprawidłowa pozycja");
    }

    @Test
    void given_fullTeam_when_createPlayer_then_throwLimitException() {

        team.setPlayers(Collections.nCopies(20, new Player()));
        PlayerRequest request = new PlayerRequest("Jan", "Nowak", 20, "GK");

        assertThatThrownBy(() -> service.createPlayer(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Drużyna osiągnęła limit 20 zawodników.");
    }

    @Test
    void given_playerBelongsToTrainer_when_updatePlayer_then_dataIsUpdated() {
        Player existing = Player.builder()
                .id(5L)
                .team(team)
                .position(PlayerPosition.DEF)
                .build();

        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));
        PlayerRequest req = new PlayerRequest("Adam", "Nowy", 22, "MID");

        service.updatePlayer(5L, req);

        assertThat(existing.getPosition()).isEqualTo(PlayerPosition.MID);
        verify(playerRepository).save(existing);
    }

    @Test
    void given_playerFromOtherTeam_when_updatePlayer_then_throwIllegalArgumentException() {

        Team other = new Team();
        other.setId(999L);

        Player existing = Player.builder().id(5L).team(other).build();
        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));

        PlayerRequest req = new PlayerRequest("Adam", "Nowy", 22, "MID");

        assertThatThrownBy(() -> service.updatePlayer(5L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nie możesz edytować zawodnika z innej drużyny.");
    }

    @Test
    void given_playerBelongsToTrainer_when_deletePlayer_then_playerIsRemoved() {

        Player existing = Player.builder().id(5L).team(team).build();
        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));

        service.deletePlayer(5L);

        verify(playerRepository).delete(existing);
    }

    @Test
    void given_playerFromOtherTeam_when_deletePlayer_then_throwIllegalArgumentException() {

        Team other = new Team();
        other.setId(777L);

        Player existing = Player.builder().id(5L).team(other).build();
        when(playerRepository.findById(5L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.deletePlayer(5L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nie możesz usuwać zawodników z innej drużyny.");
    }
}
