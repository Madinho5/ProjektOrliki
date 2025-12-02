package com.example.ProjektOrliki.trainer.controller;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.trainer.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        User trainer = currentUserService.getCurrentUser();
        return ResponseEntity.ok(trainerService.getTrainerProfile(trainer));
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestBody TrainerUpdateRequest request) {
        User trainer = currentUserService.getCurrentUser();
        return ResponseEntity.ok(trainerService.updateTrainerProfile(trainer, request));
    }
}
