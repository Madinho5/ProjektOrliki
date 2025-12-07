package com.example.ProjektOrliki.integration;

import com.example.ProjektOrliki.ProjektOrlikiApplication;
import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ProjektOrlikiApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private User trainer;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
        userRepository.deleteAll();
        trainer = User.builder()
                .username("trainer1")
                .password("pass")
                .firstName("Test")
                .lastName("Trainer")
                .role(Role.TRAINER)
                .build();

        userRepository.save(trainer);
    }

    @Test
    @WithMockUser(username = "trainer1", roles = {"TRAINER"})
    void whenValidInput_thenCreateTeam() throws Exception {

        TeamRequest request = new TeamRequest("MojaDruzyna");

        mvc.perform(
                        post("/teams")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        Team saved = teamRepository.findByTrainer(trainer).orElse(null);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("MojaDruzyna");
    }

    @Test
    @WithMockUser(username = "trainer1", roles = {"TRAINER"})
    void givenTeam_whenGetMyTeam_thenReturnTeam() throws Exception {

        Team team = Team.builder()
                .name("TestTeam")
                .trainer(trainer)
                .build();

        teamRepository.save(team);

        mvc.perform(get("/teams/mine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestTeam"));
    }
}
