package com.petverse.service.impl;

import com.petverse.client.PetServiceClient;
import com.petverse.dto.PetDTO;
import com.petverse.model.Activity;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import com.petverse.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityEventPublisher publisher;
    private final PetServiceClient petServiceClient;

    @Override
    public Activity createActivity(Activity activity, String userId) {
        // 🛡️ Güvenlik: pet gerçekten bu kullanıcıya mı ait?
        PetDTO pet = petServiceClient.getPetById(activity.getPetId());

        if (!pet.getUserId().equals(Long.parseLong(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu pet bu kullanıcıya ait değil.");
        }

        activity.setUserId(Long.parseLong(userId)); // ✅ güvenlik için tekrar set
        Activity saved = activityRepository.save(activity);

        publisher.publish("{\"type\": \"" + saved.getType() + "\", \"description\": \"" + saved.getDescription() + "\"}");

        return saved;
    }
}