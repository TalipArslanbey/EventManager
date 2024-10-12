package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.model.Registration;
import com.eventmanager.model.Role;
import com.eventmanager.model.User;
import com.eventmanager.repository.EventRepository;
import com.eventmanager.repository.RegistrationRepository;
import com.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    @Mock
    private RegistrationRepository registrationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void testRegisterUserToEvent() {
        // given
        User user =
                User.builder().username("testuser").password("password").email("test@test.com").role(Role.PARTICIPANT).build();
        Event event = Event.builder().name("Test Event").location("Dortmund").date(LocalDateTime.now()).build();
        Registration registration =
                Registration.builder().user(user).event(event).registrationDate(LocalDateTime.now()).build();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        when(eventRepository.findById(anyLong())).thenReturn(java.util.Optional.of(event));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        // when
        Registration createdRegistration = registrationService.registerUserToEvent(1L, 1L);

        // then
        assertThat(createdRegistration.getUser().getUsername()).isEqualTo("testuser");
        assertThat(createdRegistration.getEvent().getName()).isEqualTo("Test Event");

        verify(userRepository, times(1)).findById(1L);
        verify(eventRepository, times(1)).findById(1L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
        verify(kafkaProducerService, times(1)).sendEventCreatedMessage("Benutzer testuser hat sich für das Event " +
                "Test" + " Event registriert.");

    }
}
