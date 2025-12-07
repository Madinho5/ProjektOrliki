package com.example.ProjektOrliki.integration;

import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class TournamentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TournamentRepository tournamentRepository;

    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setup() {
        tournamentRepository.deleteAll();
    }

    @Test
    void givenTournament_whenGetTournamentById_thenStatus200() throws Exception {
        Tournament saved = createTestTournament(
                "Winter Cup",
                LocalDate.of(2025, 1, 10),
                8,
                TournamentStatus.CREATED
        );

        mockMvc.perform(get("/tournaments/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Winter Cup"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.teamCount").value(8));
    }

    @Test
    void whenValidInput_thenCreateTournament() throws Exception {
        Tournament t = Tournament.builder()
                .name("Summer League")
                .startDate(LocalDate.of(2025, 12, 12))
                .teamCount(8)
                .build();

        mockMvc.perform(post("/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Summer League"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.teamCount").value(8));

        List<Tournament> found = tournamentRepository.findAll();
        assertThat(found).extracting(Tournament::getName)
                .containsOnly("Summer League");
    }

    private Tournament createTestTournament(String name,
                                            LocalDate startDate,
                                            int teamCount,
                                            TournamentStatus status) {
        return tournamentRepository.save(
                Tournament.builder()
                        .name(name)
                        .startDate(startDate)
                        .teamCount(teamCount)
                        .status(status)
                        .build()
        );
    }
}
