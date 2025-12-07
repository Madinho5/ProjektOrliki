package com.example.ProjektOrliki.player.service.api;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.service.PlayerService;

public interface PlayerCreator {
    PlayerResponse createPlayer(PlayerRequest request);
}
