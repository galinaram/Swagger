package com.example.Swagger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос для отправки email")
public class EmailRequest {

    @Schema(
            description = "Email адрес получателя",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Email адрес обязателен")
    @NotBlank(message = "Email адрес не может быть пустым")
    @Email(message = "Некорректный формат email. Пример: user@example.com")
    private String toEmail;

    @Schema(
            description = "Тема письма",
            example = "Важное уведомление",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 200
    )
    @NotNull(message = "Тема письма обязательна")
    @NotBlank(message = "Тема письма не может быть пустой")
    @Size(max = 200, message = "Тема письма не должна превышать 200 символов")
    private String subject;

    @Schema(
            description = "Текст письма",
            example = "Добрый день! Это тестовое письмо.",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 5000
    )
    @NotNull(message = "Текст письма обязателен")
    @NotBlank(message = "Текст письма не может быть пустым")
    @Size(max = 5000, message = "Текст письма не должен превышать 5000 символов")
    private String text;
}