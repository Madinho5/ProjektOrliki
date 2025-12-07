package com.example.ProjektOrliki.player.controller;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.service.api.PlayerCreator;
import com.example.ProjektOrliki.player.service.api.PlayerDeleter;
import com.example.ProjektOrliki.player.service.api.PlayerModifier;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerCreator creator;
    private final PlayerModifier modifier;
    private final PlayerDeleter deleter;

    @PostMapping
    public ResponseEntity<?> createPlayer(@Valid @RequestBody PlayerRequest request) {
        PlayerResponse response = creator.createPlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        PlayerResponse response = modifier.updatePlayer(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        deleter.deletePlayer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto zawodnika o id: " + id);
    }

}

