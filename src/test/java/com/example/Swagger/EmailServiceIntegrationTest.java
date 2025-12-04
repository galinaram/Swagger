package com.example.Swagger;

import com.example.Swagger.service.EmailService;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private GreenMail greenMail;

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
    }

    @Test
    void shouldSendUserCreationEmail() throws Exception {
        String toEmail = "user@example.com";

        emailService.sendUserCreationEmail(toEmail);

        await().untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages).hasSize(1);

            MimeMessage message = receivedMessages[0];
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(toEmail);
            assertThat(message.getSubject()).isEqualTo("Аккаунт успешно создан");
            assertThat(message.getContent().toString())
                    .contains("Ваш аккаунт на сайте Test Site был успешно создан");
        });
    }

    @Test
    void shouldSendUserDeletionEmail() throws Exception {
        String toEmail = "user@example.com";

        emailService.sendUserDeletionEmail(toEmail);

        await().untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages).hasSize(1);

            MimeMessage message = receivedMessages[0];
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(toEmail);
            assertThat(message.getSubject()).isEqualTo("Аккаунт удален");
            assertThat(message.getContent().toString())
                    .contains("Ваш аккаунт был удалён");
        });
    }

    @Test
    void shouldSendCustomEmail() throws Exception {
        String toEmail = "user@example.com";
        String subject = "Test Subject";
        String text = "Test email body";

        emailService.sendCustomEmail(toEmail, subject, text);

        await().untilAsserted(() -> {
            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages).hasSize(1);

            MimeMessage message = receivedMessages[0];
            assertThat(message.getAllRecipients()[0].toString()).isEqualTo(toEmail);
            assertThat(message.getSubject()).isEqualTo(subject);
            assertThat(message.getContent().toString()).contains(text);
        });
    }

    @Test
    void shouldHandleEmailSendingFailure() {
        greenMail.stop();

        try {
            emailService.sendCustomEmail("test@example.com", "Subject", "Body");
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Failed to send email");
        } finally {
            greenMail.start();
        }
    }
}