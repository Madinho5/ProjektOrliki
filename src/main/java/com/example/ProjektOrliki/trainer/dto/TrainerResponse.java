package com.example.ProjektOrliki.trainer.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.auth.model.User}
 */
public record TrainerResponse(String firstName, String lastName, String phoneNumber) implements Serializable {
  }