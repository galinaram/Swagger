package com.example.Swagger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "notifications", itemRelation = "notification")
public class NotificationResponse extends RepresentationModel<NotificationResponse> {

    @JsonProperty("message")
    private String message;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("email")
    private String email;

    @JsonProperty("notificationType")
    private String notificationType;

    public NotificationResponse(String message, String email, String notificationType) {
        this.message = message;
        this.email = email;
        this.notificationType = notificationType;
        this.timestamp = LocalDateTime.now();
    }
}