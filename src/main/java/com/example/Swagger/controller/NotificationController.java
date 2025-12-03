package com.example.Swagger.controller;

import com.example.Swagger.dto.EmailRequest;
import com.example.Swagger.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailRequest request) {
        emailService.sendCustomEmail(request.getToEmail(), request.getSubject(), request.getText());
        return ResponseEntity.ok("Email sent successfully");
    }

    @PostMapping("/user-created")
    public ResponseEntity<String> notifyUserCreated(@RequestBody EmailRequest request) {
        emailService.sendUserCreationEmail(request.getToEmail());
        return ResponseEntity.ok("Creation email sent successfully");
    }

    @PostMapping("/user-deleted")
    public ResponseEntity<String> notifyUserDeleted(@RequestBody EmailRequest request) {
        emailService.sendUserDeletionEmail(request.getToEmail());
        return ResponseEntity.ok("Deletion email sent successfully");
    }
}