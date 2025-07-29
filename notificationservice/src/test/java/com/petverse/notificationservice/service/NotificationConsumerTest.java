package com.petverse.notificationservice.service;

import com.petverse.dto.NotificationEvent;
import com.petverse.notificationservice.model.Notification;
import com.petverse.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ActiveProfiles("test")
class NotificationConsumerTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveNotificationWhenEventReceived() {
        // arrange
        NotificationEvent event = new NotificationEvent("play", "Köpekle oyun oynandı", "42");

        // act
        notificationConsumer.handleActivity(event);

        // assert
        verify(notificationRepository, times(1)).save(notificationCaptor.capture());

        Notification savedNotification = notificationCaptor.getValue();

        assert savedNotification.getUserId().equals("42");
        assert savedNotification.getType().equals("play");
        assert savedNotification.getMessage().equals("Köpekle oyun oynandı");
        assert savedNotification.getTimestamp() != null;
    }
}
