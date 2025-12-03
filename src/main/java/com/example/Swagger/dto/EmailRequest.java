package com.example.Swagger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailRequest {

    @NotNull(message = "Email адрес обязателен")
    @NotBlank(message = "Email адрес не может быть пустым")
    @Email(message = "Некорректный формат email. Пример: user@example.com")
    private String toEmail;

    @NotNull(message = "Тема письма обязательна")
    @NotBlank(message = "Тема письма не может быть пустой")
    @Size(max = 200, message = "Тема письма не должна превышать 200 символов")
    private String subject;

    @NotNull(message = "Текст письма обязателен")
    @NotBlank(message = "Текст письма не может быть пустым")
    @Size(max = 5000, message = "Текст письма не должен превышать 5000 символов")
    private String text;
}