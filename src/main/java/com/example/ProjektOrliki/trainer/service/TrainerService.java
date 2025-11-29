package com.example.ProjektOrliki.trainer.service;
import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final UserRepository userRepository;

    public TrainerResponse getTrainerProfile(User trainer) {
        if (trainer.getRole() != Role.TRAINER) {
            throw new IllegalArgumentException("Użytkownik nie jest trenerem.");
        }

        return new TrainerResponse(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getPhoneNumber()
        );
    }

    public TrainerResponse updateTrainerProfile(User trainer, TrainerUpdateRequest request) {
        if (trainer.getRole() != Role.TRAINER) {
            throw new IllegalArgumentException("Użytkownik nie jest trenerem.");
        }

        if (request.firstName() != null) trainer.setFirstName(request.firstName());
        if (request.lastName() != null) trainer.setLastName(request.lastName());
        if (request.phoneNumber() != null) trainer.setPhoneNumber(request.phoneNumber());

        userRepository.save(trainer);

        return new TrainerResponse(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getPhoneNumber()
        );
    }
}
