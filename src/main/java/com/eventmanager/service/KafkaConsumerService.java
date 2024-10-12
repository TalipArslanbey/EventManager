package com.eventmanager.service;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "eventTopic", groupId = "group_id")
    public void consume(String message) {
        //TODO konsumieren
    }


}
