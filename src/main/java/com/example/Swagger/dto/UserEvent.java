package com.example.Swagger.dto;

import lombok.Data;

@Data
public class UserEvent {
    private String operation; // "CREATE" или "DELETE"
    private String email;
    private Long userId;

    public UserEvent() {}

    public UserEvent(String operation, String email, Long userId) {
        this.operation = operation;
        this.email = email;
        this.userId = userId;
    }
}