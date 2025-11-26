package com.example.ProjektOrliki.auth.controller;

import com.example.ProjektOrliki.auth.dto.LoginRequest;
import com.example.ProjektOrliki.auth.dto.RegisterRequest;
import com.example.ProjektOrliki.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerTrainer(@Valid @RequestBody RegisterRequest request){
        authService.registerTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Zarejestrowano trenera");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        authService.login(request);
        return ResponseEntity.ok("Zalogowano pomyślnie");
    }
}
