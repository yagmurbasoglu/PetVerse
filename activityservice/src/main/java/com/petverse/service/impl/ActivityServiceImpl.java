package com.petverse.service.impl;

import com.petverse.client.PetServiceClient;
import com.petverse.dto.PetDTO;
import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.dto.NotificationEvent;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityEventPublisher publisher;
    private final PetServiceClient petServiceClient;

    @Override
    @CircuitBreaker(name = "petService", fallbackMethod = "handlePetServiceFailure")
    public Activity createActivity(Activity activity, String userId) {
        PetDTO pet = petServiceClient.getPetById(activity.getPetId());

        if (!pet.getUserId().equals(Long.parseLong(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu pet bu kullanıcıya ait değil.");
        }

        activity.setUserId(Long.parseLong(userId));

        // Aktivite tipi boşsa hata fırlat
        if (activity.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aktivite türü (type) boş olamaz.");
        }

        // Aktivite kaydı
        Activity saved = activityRepository.save(activity);

        // Bildirim event'i oluştur ve yayınla
        NotificationEvent event = new NotificationEvent(
                saved.getType(),  // ← ENUM doğrudan geçilir
                saved.getDescription(),
                userId
        );


        publisher.publish(event);

        return saved;
    }

    public Activity handlePetServiceFailure(Activity activity, String userId, Throwable t) {
        System.out.println("🔴 FALLBACK çalıştı! PetService devre dışı: " + t.getMessage());

        throw new RuntimeException("PetService şu anda ulaşılamıyor. Lütfen daha sonra tekrar deneyin.");
    }
}
