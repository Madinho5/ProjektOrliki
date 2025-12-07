package com.example.ProjektOrliki.tournament.importer;

import com.example.ProjektOrliki.tournament.dto.ImportResultDto;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.service.api.TournamentImportHandler;
import com.example.ProjektOrliki.tournament.service.api.TournamentImporter;
import jakarta.xml.bind.JAXBContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class XmlTournamentImporter implements TournamentImporter {

    private final TournamentImportHandler handler;

    @Override
    public String supportedContentType() {
        return "text/xml";
    }

    @Override
    public ImportResultDto importData(InputStream xml) {
        int imported = 0;
        int skipped = 0;
        List<ImportResultDto.ImportError> errors = new ArrayList<>();

        try {
            JAXBContext context = JAXBContext.newInstance(TournamentXmlList.class);
            TournamentXmlList data = (TournamentXmlList) context.createUnmarshaller().unmarshal(xml);

            for (var x : data.getTournaments()) {
                try {
                    handler.handle(new TournamentRequest(
                            x.getName(),
                            LocalDate.parse(x.getStartDate()),
                            x.getTeamCount()
                    ));
                    imported++;

                } catch (Exception ex) {
                    skipped++;
                    errors.add(new ImportResultDto.ImportError(x.getName(), ex.getMessage()));
                }
            }

            return new ImportResultDto(imported, skipped, errors);

        } catch (Exception e) {
            throw new RuntimeException("Błąd importu XML: " + e.getMessage(), e);
        }
    }
}
