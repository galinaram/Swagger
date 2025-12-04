package com.example.Swagger.controller;

import com.example.Swagger.dto.EmailRequest;
import com.example.Swagger.dto.NotificationResponse;
import com.example.Swagger.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Уведомления", description = "API для отправки email-уведомлений с поддержкой HATEOAS")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    @Operation(summary = "Отправить кастомное email-сообщение")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email успешно отправлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Ошибка при отправке email")
    })
    @PostMapping("/send-email")
    public ResponseEntity<NotificationResponse> sendEmail(
            @Parameter(description = "Данные для отправки email", required = true)
            @Valid @RequestBody EmailRequest request) {

        emailService.sendCustomEmail(request.getToEmail(), request.getSubject(), request.getText());

        NotificationResponse response = new NotificationResponse(
                "Email sent successfully",
                request.getToEmail(),
                "CUSTOM_EMAIL"
        );

        // Добавляем HATEOAS ссылки
        response.add(linkTo(methodOn(NotificationController.class)
                .sendEmail(request)).withSelfRel());

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserCreated(request)).withRel("notify-creation"));

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserDeleted(request)).withRel("notify-deletion"));

        response.add(linkTo(methodOn(NotificationController.class)
                .getNotificationRoot()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Уведомить о создании пользователя")
    @PostMapping("/user-created")
    public ResponseEntity<NotificationResponse> notifyUserCreated(
            @Parameter(description = "Данные для отправки email", required = true)
            @Valid @RequestBody EmailRequest request) {

        emailService.sendUserCreationEmail(request.getToEmail());

        NotificationResponse response = new NotificationResponse(
                "User creation email sent successfully",
                request.getToEmail(),
                "USER_CREATED"
        );

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserCreated(request)).withSelfRel());

        response.add(linkTo(methodOn(NotificationController.class)
                .sendEmail(request)).withRel("send-custom-email"));

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserDeleted(request)).withRel("notify-deletion"));

        response.add(linkTo(methodOn(NotificationController.class)
                .getNotificationRoot()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Уведомить об удалении пользователя")
    @PostMapping("/user-deleted")
    public ResponseEntity<NotificationResponse> notifyUserDeleted(
            @Parameter(description = "Данные для отправки email", required = true)
            @Valid @RequestBody EmailRequest request) {

        emailService.sendUserDeletionEmail(request.getToEmail());

        NotificationResponse response = new NotificationResponse(
                "User deletion email sent successfully",
                request.getToEmail(),
                "USER_DELETED"
        );

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserDeleted(request)).withSelfRel());

        response.add(linkTo(methodOn(NotificationController.class)
                .sendEmail(request)).withRel("send-custom-email"));

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserCreated(request)).withRel("notify-creation"));

        response.add(linkTo(methodOn(NotificationController.class)
                .getNotificationRoot()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Корневой ресурс уведомлений", description = "Возвращает ссылки на все доступные операции")
    @GetMapping
    public ResponseEntity<NotificationResponse> getNotificationRoot() {
        EmailRequest sampleRequest = new EmailRequest();
        sampleRequest.setToEmail("user@example.com");
        sampleRequest.setSubject("Sample Subject");
        sampleRequest.setText("Sample Text");

        NotificationResponse response = new NotificationResponse(
                "Notification API Root",
                null,
                "API_ROOT"
        );

        // Добавляем ссылки на все доступные операции
        response.add(linkTo(methodOn(NotificationController.class)
                .getNotificationRoot()).withSelfRel());

        response.add(linkTo(methodOn(NotificationController.class)
                .sendEmail(sampleRequest)).withRel("send-email"));

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserCreated(sampleRequest)).withRel("user-created"));

        response.add(linkTo(methodOn(NotificationController.class)
                .notifyUserDeleted(sampleRequest)).withRel("user-deleted"));

        return ResponseEntity.ok(response);
    }
}