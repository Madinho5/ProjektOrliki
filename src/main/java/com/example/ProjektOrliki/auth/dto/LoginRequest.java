package com.example.ProjektOrliki.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.auth.model.User}
 */
@Value
public class LoginRequest implements Serializable {
    @NotBlank(message = "Nazwa nie może być pusta")
    String username;

    @NotBlank(message = "Nazwa nie może być pusta")
    String password;
}