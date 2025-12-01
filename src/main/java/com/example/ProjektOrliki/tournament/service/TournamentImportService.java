package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.tournament.dto.ImportResultDto;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.importer.TournamentXmlList;
import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentImportService {

    private final TournamentService tournamentService;

    public ImportResultDto importXml(InputStream xml) {
        int imported = 0;
        int skipped = 0;
        List<ImportResultDto.ImportError> errors = new ArrayList<>();

        try {
            JAXBContext context = JAXBContext.newInstance(TournamentXmlList.class);
            TournamentXmlList data = (TournamentXmlList) context.createUnmarshaller().unmarshal(xml);

            for (var x : data.getTournaments()) {
                try {
                    tournamentService.create(
                            new TournamentRequest(
                                    x.getName(),
                                    LocalDate.parse(x.getStartDate()),
                                    x.getTeamCount()
                            )
                    );
                    imported++;
                } catch (Exception e) {
                    skipped++;
                    errors.add(new ImportResultDto.ImportError(x.getName(), e.getMessage()));
                }
            }

            return new ImportResultDto(imported, skipped, errors);

        } catch (Exception e) {
            throw new RuntimeException("Błąd importu XML: " + e.getMessage(), e);
        }
    }
}
