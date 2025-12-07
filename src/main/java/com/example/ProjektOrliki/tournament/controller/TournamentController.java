package com.example.ProjektOrliki.tournament.controller;

import com.example.ProjektOrliki.tournament.dto.*;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.service.*;
import com.example.ProjektOrliki.tournament.service.api.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Validated
public class TournamentController {

    private final TournamentCreator tournamentCreator;
    private final TournamentReader tournamentReader;
    private final TournamentModifier tournamentModifier;
    private final TournamentLifecycle tournamentLifecycle;
    private final TournamentRegistration tournamentRegistration;
    private final TournamentImportService tournamentImportService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TournamentRequest request) {
        return ResponseEntity.status(CREATED).body(tournamentCreator.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentReader.getById(id));
    }

    @GetMapping
    public ResponseEntity<?> getByStatus(@RequestParam TournamentStatus status) {
        return ResponseEntity.ok(tournamentReader.getByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody TournamentRequest request) {
        return ResponseEntity.ok(tournamentModifier.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tournamentModifier.delete(id);
        return ResponseEntity.ok("Usunięto turniej: " + id);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody TournamentStatusRequest request) {
        return ResponseEntity.ok(tournamentLifecycle.updateStatus(id, request.status()));
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerTeam(@PathVariable Long id) {
        tournamentRegistration.registerTeam(id);
        return ResponseEntity.status(CREATED).body("Drużyna zgłoszona.");
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importFile(@RequestParam MultipartFile file) throws IOException {
        ImportResultDto result = tournamentImportService.importFile(
                file.getInputStream(),
                file.getContentType()
        );
        return ResponseEntity.ok(result);
    }
}
