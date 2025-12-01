package com.example.ProjektOrliki.tournament.controller;

import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.match.model.Match;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.tournament.dto.*;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.service.BracketService;
import com.example.ProjektOrliki.tournament.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final BracketService bracketService;
    private final MatchRepository matchRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentResponse create(@Valid @RequestBody TournamentRequest request) {
        return tournamentService.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TournamentDetailsResponse findById(@PathVariable Long id) {
        return tournamentService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TournamentResponse update(@PathVariable Long id, @Valid @RequestBody TournamentRequest request) {
        return tournamentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto turniej o id: " + id );
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TournamentResponse updateStatus(@PathVariable Long id, @RequestBody TournamentStatusRequest request) {
        return tournamentService.updateStatus(id,request.status());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TournamentResponse> getByStatus(
            @RequestParam TournamentStatus status
    ) {
        return tournamentService.getByStatus(status);
    }

    @PostMapping("/{id}/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerTeam(@PathVariable Long id) {
        tournamentService.registerTeam(id);
        return "Drużyna zgłoszona do turnieju.";
    }

    @GetMapping("/{id}/matches")
    @ResponseStatus(HttpStatus.OK)
    public BracketDto getBracket(@PathVariable Long id) {
        List<Match> matches = matchRepository.findByTournamentId(id);
        Map<Integer, List<MatchDto>> grouped = matches.stream()
                .map(m -> new MatchDto(
                        m.getId(),
                        m.getMatchNumber(),
                        m.getTeamA().getName(),
                        m.getTeamB().getName(),
                        m.getScoreA(),
                        m.getScoreB(),
                        m.getWinner().getName()
                ))
                .collect(Collectors.groupingBy(
                        MatchDto::getMatchNumber,
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<BracketRoundDto> rounds = grouped.entrySet().stream()
                .map(e -> new BracketRoundDto(e.getKey(), e.getValue()))
                .toList();

        return new BracketDto(rounds);
    }

    @PostMapping("/{id}/next-round")
    @ResponseStatus(HttpStatus.OK)
    public List<MatchDto> generateNextRound(@PathVariable Long id) {
        return bracketService.generateNextRound(id);
    }


}
