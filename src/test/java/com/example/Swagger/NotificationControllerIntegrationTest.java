package com.example.Swagger;

import com.example.Swagger.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GreenMail greenMail;

    private EmailRequest emailRequest;

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();

        emailRequest = new EmailRequest();
        emailRequest.setToEmail("test@example.com");
        emailRequest.setSubject("Test Subject");
        emailRequest.setText("Test email content");
    }

    @Test
    void shouldSendCustomEmail() throws Exception {
        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email sent successfully"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.notificationType").value("CUSTOM_EMAIL"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.notify-creation").exists())
                .andExpect(jsonPath("$._links.notify-deletion").exists());

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).isEqualTo("Test Subject");
        });
    }

    @Test
    void shouldNotifyUserCreated() throws Exception {
        mockMvc.perform(post("/api/notifications/user-created")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User creation email sent successfully"))
                .andExpect(jsonPath("$.notificationType").value("USER_CREATED"));

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).isEqualTo("Аккаунт успешно создан");
        });
    }

    @Test
    void shouldNotifyUserDeleted() throws Exception {
        mockMvc.perform(post("/api/notifications/user-deleted")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deletion email sent successfully"))
                .andExpect(jsonPath("$.notificationType").value("USER_DELETED"));

        await().untilAsserted(() -> {
            MimeMessage[] messages = greenMail.getReceivedMessages();
            assertThat(messages).hasSize(1);
            assertThat(messages[0].getSubject()).isEqualTo("Аккаунт удален");
        });
    }

    @Test
    void shouldReturnNotificationRoot() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Notification API Root"))
                .andExpect(jsonPath("$.notificationType").value("API_ROOT"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.send-email").exists())
                .andExpect(jsonPath("$._links.user-created").exists())
                .andExpect(jsonPath("$._links.user-deleted").exists());
    }

    @Test
    void shouldValidateEmailRequest() throws Exception {
        // Given - invalid request
        EmailRequest invalidRequest = new EmailRequest();
        invalidRequest.setToEmail("invalid-email");
        invalidRequest.setSubject("");
        invalidRequest.setText("");

        mockMvc.perform(post("/api/notifications/send-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnHateoasLinks() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertThat(content).contains("_links");
        assertThat(content).contains("self");
        assertThat(content).contains("send-email");
        assertThat(content).contains("user-created");
        assertThat(content).contains("user-deleted");
    }
}