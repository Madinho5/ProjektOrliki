package com.example.ProjektOrliki.trainer.service;
import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.service.CurrentUserService;
import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.auth.repository.UserRepository;

import com.example.ProjektOrliki.trainer.service.api.TrainerModifier;
import com.example.ProjektOrliki.trainer.service.api.TrainerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerService implements TrainerReader, TrainerModifier {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    public TrainerResponse getMyProfile() {
        User trainer = currentUserService.getCurrentUser();

        if (trainer.getRole() != Role.TRAINER) {
            throw new IllegalArgumentException("Użytkownik nie jest trenerem.");
        }

        return new TrainerResponse(
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getPhoneNumber()
        );
    }

    @Override
    public TrainerResponse updateMyProfile(TrainerUpdateRequest request) {
        User trainer = currentUserService.getCurrentUser();

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
