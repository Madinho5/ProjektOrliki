package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository repository;
    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;

    public TournamentResponse create(TournamentRequest request) {
        if(repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .status(TournamentStatus.CREATED)
                .teamCount(request.getTeamCount())
                .build();
        Tournament savedTournament = repository.save(tournament);
        return toResponse(savedTournament);
    }

    public TournamentDetailsResponse getById(Long id) {
        Tournament t = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));
        return toDetailsResponse(t);
    }

    public List<TournamentResponse> getByStatus(TournamentStatus status) {
        return repository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
    }
    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        if(!tournament.getName().equals(request.getName()) && repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        tournament.setName(request.getName());
        tournament.setStartDate(request.getStartDate());
        tournament.setTeamCount(request.getTeamCount());

        Tournament updatedTournament = repository.save(tournament);
        return toResponse(updatedTournament);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Nie znaleziono turnieju o id: " + id);
        }
        repository.deleteById(id);
    }

    public TournamentResponse updateStatus(Long id, TournamentStatus newStatus) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        tournament.setStatus(newStatus);

        return toResponse(repository.save(tournament));
    }

    private TournamentResponse toResponse(Tournament t) {
        return new TournamentResponse(
                t.getId(),
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount()
        );
    }

    private TournamentDetailsResponse toDetailsResponse(Tournament t) {

        List<TournamentDetailsResponse.TeamDto> teams = t.getTeams().stream()
                .map(team -> new TournamentDetailsResponse.TeamDto(team.getId(), team.getName()))
                .toList();

        Long winnerId = (t.getWinner() != null) ? t.getWinner().getId() : null;
        String winnerName = (t.getWinner() != null) ? t.getWinner().getName() : null;

        return new TournamentDetailsResponse(
                t.getId(),
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount(),
                teams,
                winnerId,
                winnerName
        );
    }

    public void registerTeam(Long tournamentId) {
        User trainer = currentUserService.getCurrentUser();

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
