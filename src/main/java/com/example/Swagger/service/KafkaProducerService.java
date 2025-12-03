package com.example.Swagger.service;

import com.example.Swagger.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private static final String TOPIC = "user-events";

    @Autowired
    private KafkaTemplate<String, UserEvent> kafkaTemplate;

    public void sendUserEvent(String operation, String email, Long userId) {
        UserEvent event = new UserEvent(operation, email, userId);
        kafkaTemplate.send(TOPIC, event);
        log.info("Sent user event to Kafka: {} for user: {}", operation, email);
    }
}