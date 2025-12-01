package com.example.ProjektOrliki.tournament.dto;

import java.util.List;

public record ImportResultDto(
        int imported,
        int skipped,
        List<ImportError> errors
) {
    public record ImportError(String name, String reason) {}
}
