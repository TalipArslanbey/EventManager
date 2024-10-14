package com.eventmanager.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "eventTopic", groupId = "groupId")
    public void consume(String message) {
        log.info(message);
    }


}
