package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.mapper.TournamentMapper;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import com.example.ProjektOrliki.tournament.service.api.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class TournamentService implements
        TournamentCreator,
        TournamentReader,
        TournamentModifier,
        TournamentLifecycle,
        TournamentRegistration {

    private final TournamentRepository repository;
    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;
    private final TournamentMapper tournamentMapper;
    private final MatchRepository matchRepository;

    private boolean isTeamCountValid(int n) {
        return n > 0 && ((n & (n - 1)) == 0);
    }

    public TournamentResponse create(@Valid TournamentRequest request) {
        if(repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        if (!isTeamCountValid(request.getTeamCount())) {
            throw new IllegalArgumentException("Liczba drużyn musi być jedną z: 2, 4, 8 lub 16.");
        }

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .status(TournamentStatus.CREATED)
                .teamCount(request.getTeamCount())
                .build();

        Tournament saved = repository.save(tournament);
        return tournamentMapper.toResponse(saved);
    }

    public TournamentDetailsResponse getById(Long id) {
        Tournament t = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        return tournamentMapper.toDetailsResponse(t);
    }

    public List<TournamentResponse> getByStatus(TournamentStatus status) {
        return repository.findByStatus(status).stream()
                .map(tournamentMapper::toResponse)
                .toList();
    }

    public TournamentResponse update(Long id, @Valid TournamentRequest request) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        if (tournament.getStatus() != TournamentStatus.CREATED && tournament.getStatus() != TournamentStatus.REGISTRATION_OPENED) {
            throw new IllegalArgumentException("Nie można edytować tego turnieju");
        }

        if(!tournament.getName().equals(request.getName()) && repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        if (tournament.getTeams().size() > request.getTeamCount()) {
            throw new IllegalArgumentException("Nowy limit drużyn nie może być mniejszy niż liczba aktualnie zgłoszonych.");
        }

        tournament.setName(request.getName());
        tournament.setStartDate(request.getStartDate());
        tournament.setTeamCount(request.getTeamCount());

        return tournamentMapper.toResponse(repository.save(tournament));
    }

    @Transactional
    public void delete(Long id) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        if (tournament.getStatus() == TournamentStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Turniej nie może zostać usunięty na tym etapie.");
        }

        matchRepository.deleteByTournamentId(id);

        tournament.getTeams().clear();
        repository.save(tournament);

        repository.delete(tournament);
    }

    public TournamentResponse updateStatus(Long id, TournamentStatus newStatus) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        tournament.setStatus(newStatus);

        return tournamentMapper.toResponse(repository.save(tournament));
    }

    public void registerTeam(Long tournamentId) {
        var trainer = currentUserService.getCurrentUser();

        Tournament tournament = repository.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + tournamentId));

        if (tournament.getStatus() != TournamentStatus.REGISTRATION_OPENED) {
            throw new IllegalArgumentException("Rejestracja na ten turniej jest zamknięta.");
        }

        Team team = teamRepository.findByTrainer(trainer)
                .orElseThrow(() -> new IllegalStateException("Trener nie ma przypisanej drużyny."));

        if (tournament.getTeams().contains(team)) {
            throw new IllegalArgumentException("Drużyna jest już zgłoszona do tego turnieju.");
        }

        if (tournament.getTeams().size() >= tournament.getTeamCount()) {
            throw new IllegalArgumentException("Osiągnięto limit drużyn.");
        }

        int playerCount = team.getPlayers().size();
        if (playerCount < 7) {
            throw new IllegalArgumentException("Drużyna musi mieć co najmniej 7 zawodników.");
        }
        if (playerCount > 10) {
            throw new IllegalArgumentException("Drużyna może mieć maksymalnie 10 zawodników.");
        }

        boolean hasGK = team.getPlayers().stream()
                .anyMatch(p -> p.getPosition() == PlayerPosition.GK);

        if (!hasGK) {
            throw new IllegalArgumentException("Drużyna musi mieć przynajmniej jednego bramkarza.");
        }

        tournament.getTeams().add(team);

        if (tournament.getTeams().size() == tournament.getTeamCount()) {
            tournament.setStatus(TournamentStatus.REGISTRATION_CLOSED);
        }

        repository.save(tournament);
    }
}
