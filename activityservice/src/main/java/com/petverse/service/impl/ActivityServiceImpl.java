package com.petverse.service.impl;

import com.petverse.client.PetServiceClient;
import com.petverse.client.WeatherClient;
import com.petverse.dto.PetDTO;
import com.petverse.dto.WeatherResponse;
import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.dto.NotificationEvent;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.ActivityService;
import com.petverse.service.WeatherAdviceService;
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
    private final WeatherClient weatherClient;
    private final WeatherAdviceService weatherAdviceService;

    @Override
    @CircuitBreaker(name = "petService", fallbackMethod = "handlePetServiceFailure")
    public Activity createActivity(Activity activity, String userId) {
        // Pet kontrolü
        PetDTO pet = petServiceClient.getPetById(activity.getPetId());

        if (!pet.getUserId().equals(Long.parseLong(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu pet bu kullanıcıya ait değil.");
        }

        activity.setUserId(Long.parseLong(userId));

        if (activity.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aktivite türü (type) boş olamaz.");
        }

        // Yürüyüşse hava durumu kontrolü
        if (activity.getType() == ActivityType.WALK) {
            WeatherResponse response = weatherClient.getWeather("Ankara");

            if (response == null || response.getCurrentWeather() == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Hava durumu bilgisine ulaşılamadı.");
            }

            String advice = weatherAdviceService.generateAdvice(response.getCurrentWeather());

            if (advice.contains("uygun değil") || advice.contains("çok sıcak")
                    || advice.contains("soğuk") || advice.contains("Rüzgar kuvvetli")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Yürüyüş iptal: " + advice);
            }
        }

        // Aktivite kaydı
        Activity saved = activityRepository.save(activity);

        // Bildirim event'i yayınla
        NotificationEvent event = new NotificationEvent(
                saved.getType(),
                saved.getDescription(),
                userId
        );
        publisher.publish(event);

        return saved;
    }

    // Fallback metodu (Resilience4j)
    public Activity handlePetServiceFailure(Activity activity, String userId, Throwable t) {
        System.out.println("🔴 FALLBACK çalıştı! PetService devre dışı: " + t.getMessage());
        throw new RuntimeException("PetService şu anda ulaşılamıyor. Lütfen daha sonra tekrar deneyin.");
    }
}
