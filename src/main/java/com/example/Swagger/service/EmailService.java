package com.example.Swagger.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Value("${app.email.from:no-reply@example.com}")
    private String fromEmail;

    @Value("${app.email.site-name:Наш сайт}")
    private String siteName;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendUserCreationEmail(String toEmail) {
        String subject = "Аккаунт успешно создан";
        String text = String.format("Здравствуйте! Ваш аккаунт на сайте %s был успешно создан.", siteName);
        sendEmail(toEmail, subject, text);
    }

    public void sendUserDeletionEmail(String toEmail) {
        String subject = "Аккаунт удален";
        String text = "Здравствуйте! Ваш аккаунт был удалён.";
        sendEmail(toEmail, subject, text);
    }

    public void sendCustomEmail(String toEmail, String subject, String text) {
        sendEmail(toEmail, subject, text);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            log.info("Attempting to send email to: {}, subject: {}", toEmail, subject);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage());
            if (isTestEnvironment()) {
                log.warn("In test environment - ignoring email sending failure");
            } else {
                throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
            }
        }
    }

    private boolean isTestEnvironment() {
        return "test".equals(System.getProperty("spring.profiles.active")) ||
                "test".equals(System.getenv("SPRING_PROFILES_ACTIVE"));
    }
}