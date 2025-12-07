package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.ImportResultDto;
import java.io.InputStream;

public interface TournamentImporter {
    ImportResultDto importData(InputStream inputStream);
    String supportedContentType();
}
