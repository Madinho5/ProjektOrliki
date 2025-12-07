package com.example.ProjektOrliki.team.controller;

import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.service.api.TeamCreator;
import com.example.ProjektOrliki.team.service.api.TeamModifier;
import com.example.ProjektOrliki.team.service.api.TeamReader;
import com.example.ProjektOrliki.team.service.api.TeamRemover;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamCreator teamCreator;
    private final TeamReader teamReader;
    private final TeamModifier teamModifier;
    private final TeamRemover teamRemover;

    @PostMapping
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamCreator.create(request));
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyTeam() {
        return ResponseEntity.ok(teamReader.getMyTeam());
    }

    @PutMapping("/mine")
    public ResponseEntity<?> updateMyTeam(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamModifier.updateMyTeam(request));
    }

    @DeleteMapping("/mine")
    public ResponseEntity<?> deleteMyTeam() {
        teamRemover.deleteMyTeam();
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto drużynę");
    }
}
