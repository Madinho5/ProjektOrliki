package com.example.ProjektOrliki.trainer.service.api;

import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import jakarta.validation.Valid;

public interface TrainerModifier {
    TrainerResponse updateMyProfile(@Valid TrainerUpdateRequest request);
}
