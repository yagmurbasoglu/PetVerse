package com.petverse.service;

import com.petverse.model.Activity;
import com.petverse.producer.ActivityEventPublisher;
import com.petverse.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityEventPublisher publisher;

    public Activity createActivity(Activity activity) {
        Activity saved = activityRepository.save(activity);
        publisher.publish("{\"type\": \"" + saved.getType() + "\", \"description\": \""+ saved.getDescription() +"\"}");
        return saved;
    }
}
