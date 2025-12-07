package com.example.ProjektOrliki.trainer.controller;

import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.trainer.service.api.TrainerModifier;
import com.example.ProjektOrliki.trainer.service.api.TrainerReader;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerReader trainerReader;
    private final TrainerModifier trainerModifier;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        return ResponseEntity.ok(trainerReader.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@Valid @RequestBody TrainerUpdateRequest request) {
        return ResponseEntity.ok(trainerModifier.updateMyProfile(request));
    }
}
