package com.example.ProjektOrliki.integration;

import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void setup() {
        tournamentRepository.deleteAll();
    }

    @Test
    void shouldReturnTournamentById() throws Exception {

        Tournament saved = tournamentRepository.save(
                Tournament.builder()
                        .name("Winter Cup")
                        .startDate(java.time.LocalDate.of(2025, 1, 10))
                        .status(TournamentStatus.CREATED)
                        .teamCount(8)
                        .build()
        );

        mockMvc.perform(get("/tournaments/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Winter Cup"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.teamCount").value(8));
    }

    @Test
    void shouldCreateTournament() throws Exception {
        String json = """
            {
              "name": "Summer League",
              "startDate": "2025-12-12",
              "teamCount": 8
            }
            """;

        mockMvc.perform(post("/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Summer League"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.teamCount").value(8));
    }

}
