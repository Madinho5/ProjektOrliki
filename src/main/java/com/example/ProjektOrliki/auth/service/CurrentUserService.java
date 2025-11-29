package com.example.ProjektOrliki.auth.service;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Nie znaleziono uzytkownika"));
    }
}
