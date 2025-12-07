package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.tournament.dto.ImportResultDto;
import com.example.ProjektOrliki.tournament.service.api.TournamentImporter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentImportService {

    private final List<TournamentImporter> importers;

    public ImportResultDto importFile(InputStream file, String contentType) {
        return importers.stream()
                .filter(i -> i.supportedContentType().equals(contentType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nieobsługiwany format pliku: " + contentType))
                .importData(file);
    }
}
