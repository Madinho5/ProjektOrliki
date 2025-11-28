package com.example.ProjektOrliki.trainer.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.trainer.model.Trainer;
import com.example.ProjektOrliki.trainer.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository repository;

    public void createTrainer(User user, String firstName, String lastName) {
        repository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Trainer trainer = Trainer.builder()
                            .user(user)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phoneNumber(null)
                            .build();
                    return repository.save(trainer);
                });
    }
}
