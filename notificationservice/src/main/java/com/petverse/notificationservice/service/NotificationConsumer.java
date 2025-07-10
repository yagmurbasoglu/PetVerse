package com.petverse.notificationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petverse.notificationservice.model.Notification;
import com.petverse.notificationservice.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository repository;

    @RabbitListener(queues = "activity_queue")
    public void handleActivityMessage(String rawMessage) {
        System.out.println("NotificationService mesaj aldı: " + rawMessage);

        // JSON parse etmeye çalış
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(rawMessage);

            System.out.println("JSON mesaj: " + jsonNode.toString());

            String type = jsonNode.get("type").asText();
            String description = jsonNode.get("description").asText();

            System.out.println("Type: " + type);
            System.out.println("Description: " + description);

            Notification notification = new Notification();
            notification.setType(type);
            notification.setMessage(description);
            
            repository.save(notification);

            System.out.println("NotificationService mesajı veritabanına kaydetti: " + notification.toString());

        } catch (Exception e) {
            e.printStackTrace();

            // Düz string ise direkt ekle
            Notification notification = new Notification();
            notification.setMessage(rawMessage);
            notification.setType("activity");

            repository.save(notification);
        }

        System.out.println("NotificationService mesajı işleme tamamlandı.");

    }
}
