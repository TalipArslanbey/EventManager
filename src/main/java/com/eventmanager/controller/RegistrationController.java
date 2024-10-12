package com.eventmanager.controller;

import com.eventmanager.model.Registration;
import com.eventmanager.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public ResponseEntity<List<Registration>> getAllRegistrations() {
        List<Registration> registrations = registrationService.getAllRegistrations();
        return ResponseEntity.ok(registrations);
    }

    @PostMapping
    public ResponseEntity<Registration> registerUserToEvent(@RequestParam Long userId, @RequestParam Long eventId) {
        Registration registration = registrationService.registerUserToEvent(userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registration);
    }
}
