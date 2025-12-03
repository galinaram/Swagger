package com.example.Swagger.service;

import com.example.Swagger.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEventConsumer {

    private final EmailService emailService;

    @Autowired
    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events")
    public void consumeUserEvent(UserEvent event) {
        log.info("Received user event: {} for email: {}", event.getOperation(), event.getEmail());

        try {
            switch (event.getOperation().toUpperCase()) {
                case "CREATE":
                    emailService.sendUserCreationEmail(event.getEmail());
                    break;
                case "DELETE":
                    emailService.sendUserDeletionEmail(event.getEmail());
                    break;
                default:
                    log.warn("Unknown operation: {}", event.getOperation());
            }
            log.info("Email sent successfully for: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send email for: {}", event.getEmail(), e);
        }
    }
}