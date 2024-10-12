package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final KafkaProducerService kafkaProducerService;

    public EventService(EventRepository eventRepository, KafkaProducerService kafkaProducerService) {
        this.eventRepository = eventRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Cacheable(value = "events", key = "#id")
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Event mit ID " + id + " nicht gefunden"));
    }

    public Event createEvent(Event event) {
        Event newEvent = eventRepository.save(event);
        kafkaProducerService.sendEventCreatedMessage("Neues Event erstellt: " + newEvent.getName());
        return newEvent;
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

}
