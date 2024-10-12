package com.eventmanager.service;

import com.eventmanager.model.Event;
import com.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @InjectMocks
    private EventService eventService;

    @Test
    void testCreateEvent() {
        // given
        Event event =
                Event.builder().name("Test Event").description("Beschreibung").location("Online").date(LocalDateTime.now()).build();
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // when
        Event createdEvent = eventService.createEvent(event);

        // then
        assertThat(createdEvent.getName()).isEqualTo("Test Event");
        verify(eventRepository, times(1)).save(event);
        verify(kafkaProducerService, times(1)).sendEventCreatedMessage("Neues Event erstellt: " + event.getName());

    }
}