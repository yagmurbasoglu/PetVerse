package com.petverse.notificationservice.controller;

import com.petverse.notificationservice.model.Notification;
import com.petverse.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    void shouldReturnNotificationsForUser() throws Exception {
        // arrange
        Notification notif = Notification.builder()
                .id(1L)
                .userId("42")
                .type("info")
                .message("Bildirim mesajı")
                .timestamp(LocalDateTime.now())
                .build();

        when(notificationRepository.findByUserIdOrderByTimestampDesc("42"))
                .thenReturn(List.of(notif));

        // act + assert
        mockMvc.perform(get("/notifications")
                        .header("X-User-Id", "42")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("42"))
                .andExpect(jsonPath("$[0].message").value("Bildirim mesajı"));
    }
}
