package com.eventmanager.service;


import com.eventmanager.model.Event;
import com.eventmanager.model.Registration;
import com.eventmanager.model.User;
import com.eventmanager.repository.EventRepository;
import com.eventmanager.repository.RegistrationRepository;
import com.eventmanager.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final KafkaProducerService kafkaProducerService;


    public RegistrationService(RegistrationRepository registrationRepository, UserRepository userRepository, EventRepository eventRepository, KafkaProducerService kafkaProducerService) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Registration registerUserToEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Benutzer mit ID " + userId + " nicht gefunden"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event mit ID " + eventId + " nicht gefunden"));

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registrationDate(LocalDateTime.now())
                .build();

        Registration savedRegistration = registrationRepository.save(registration);

        kafkaProducerService.sendEventCreatedMessage("Benutzer " + user.getUsername() + " hat sich für das Event " + event.getName() + " registriert.");

        return savedRegistration;

    }

}
