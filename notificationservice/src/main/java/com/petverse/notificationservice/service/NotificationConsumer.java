package com.petverse.notificationservice.service;

import com.petverse.notificationservice.model.Notification;
import com.petverse.dto.NotificationEvent;
import com.petverse.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository repository;

    @RabbitListener(queues = "activity_queue")
    public void handleActivity(NotificationEvent event) {
        System.out.println("NotificationService event aldı: " + event);

        Notification notification = new Notification();
        notification.setType(event.getType());
        notification.setMessage(event.getDescription());
        notification.setUserId(event.getUserId());
        notification.setTimestamp(LocalDateTime.now());

        repository.save(notification);

        System.out.println("Notification kaydedildi: " + notification);
    }
}
