package com.example.ProjektOrliki.tournament.importer;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import java.util.List;

@Getter
@XmlRootElement(name = "tournaments")
@XmlAccessorType(XmlAccessType.FIELD)
public class TournamentXmlList {

    @XmlElement(name = "tournament")
    private List<TournamentXml> tournaments;

    @Getter
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TournamentXml {
        private String name;
        private String startDate;
        private Integer teamCount;
    }
}
