package com.example.ProjektOrliki.trainer.service.api;

import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import com.example.ProjektOrliki.auth.model.User;

public interface TrainerReader {
    TrainerResponse getMyProfile();
}
