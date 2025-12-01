package com.example.ProjektOrliki.trainer.controller;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.trainer.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final UserRepository userRepository;

    private User getCurrentTrainer() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono użytkownika."));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public TrainerResponse getMyProfile() {
        User trainer = getCurrentTrainer();
        return trainerService.getTrainerProfile(trainer);
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public TrainerResponse updateMyProfile(@Valid @RequestBody TrainerUpdateRequest request) {
        User trainer = getCurrentTrainer();
        return trainerService.updateTrainerProfile(trainer, request);
    }
}
