package com.example.ProjektOrliki;

import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.trainer.dto.TrainerResponse;
import com.example.ProjektOrliki.trainer.dto.TrainerUpdateRequest;
import com.example.ProjektOrliki.trainer.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TrainerServiceTest {
    private TrainerService service;
    private UserRepository userRepository;

    private User trainer;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UserRepository.class);
        service = new TrainerService(userRepository);

        trainer = new User();
        trainer.setRole(Role.TRAINER);
        trainer.setFirstName("Adam");
        trainer.setLastName("Nowak");
        trainer.setPhoneNumber("123456789");
    }

    @Test
    void given_trainerRole_when_getTrainerProfile_then_returnProfile(){

        TrainerResponse response = service.getTrainerProfile(trainer);

        assertThat(response.firstName()).isEqualTo("Adam");
        assertThat(response.lastName()).isEqualTo("Nowak");
        assertThat(response.phoneNumber()).isEqualTo("123456789");
    }

    @Test
    void given_nonTrainerRole_when_getTrainerProfile_then_throwException(){

        trainer.setRole(Role.ADMIN);

        assertThatThrownBy(() -> service.getTrainerProfile(trainer)).isInstanceOf(IllegalArgumentException.class).hasMessage("Użytkownik nie jest trenerem.");
    }

    @Test
    void given_validUpdateRequest_when_updateTrainerProfile_then_saveAndReturnUpdatedProfile(){

        TrainerUpdateRequest request = new TrainerUpdateRequest("Jan", "Kowalski", "987654321");

        TrainerResponse response = service.updateTrainerProfile(trainer, request);

        assertThat(response.firstName()).isEqualTo("Jan");
        assertThat(response.lastName()).isEqualTo("Kowalski");
        assertThat(response.phoneNumber()).isEqualTo("987654321");

        verify(userRepository).save(trainer);
    }

    @Test
    void given_nullFields_when_updateTrainerProfile_then_onlyNonNullValuesAreUpdated() {

        TrainerUpdateRequest req = new TrainerUpdateRequest(null, "Kowal", null);

        TrainerResponse response = service.updateTrainerProfile(trainer, req);

        assertThat(response.firstName()).isEqualTo("Adam");
        assertThat(response.lastName()).isEqualTo("Kowal");
        assertThat(response.phoneNumber()).isEqualTo("123456789");

        verify(userRepository).save(trainer);
    }
}
