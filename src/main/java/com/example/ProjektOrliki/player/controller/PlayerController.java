package com.example.ProjektOrliki.player.controller;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<?> createPlayer(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse response = playerService.createPlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        PlayerResponse response = playerService.updatePlayer(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto zawodnika o id: " + id);
    }

}

