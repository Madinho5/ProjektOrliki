package com.example.ProjektOrliki.auth.service;

import com.example.ProjektOrliki.auth.dto.LoginRequest;
import com.example.ProjektOrliki.auth.dto.RegisterRequest;
import com.example.ProjektOrliki.auth.exception.UserAlreadyExistsException;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.trainer.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerTrainer(RegisterRequest request){
        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Taki uzytkownik juz istnieje");
        }

    User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.TRAINER)
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phoneNumber(null)
            .build();

        userRepository.save(user);
    }

    public void login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Niepoprawna nazwa uzytkownika"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Niepoprawne hasło");
        }
    }
}
