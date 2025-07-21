package com.petverse.controller;

import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<Activity> createActivity(
            @RequestBody Activity activity,
            @RequestHeader("X-User-Id") String userId) {

        Activity saved = activityService.createActivity(activity, userId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/types")
    public ResponseEntity<ActivityType[]> getActivityTypes() {
        return ResponseEntity.ok(ActivityType.values());
    }

    // Ortak metod: Öntanımlı açıklamayla enum type içeren Activity oluşturur
    private ResponseEntity<Activity> createTypedActivity(
            ActivityType type,
            String defaultDescription,
            String userId,
            Long petId,
            String description) {

        Activity activity = Activity.builder()
                .type(type)
                .description(description != null ? description : defaultDescription)
                .petId(petId)
                .build();

        // userId sadece dışarıdan güvenlik için geldiğinden ayrı parametre
        return ResponseEntity.ok(activityService.createActivity(activity, userId));
    }

    @PostMapping("/feed")
    public ResponseEntity<Activity> feed(@RequestParam Long petId,
                                         @RequestHeader("X-User-Id") String userId,
                                         @RequestBody(required = false) String description) {
        return createTypedActivity(ActivityType.FEED, "Mama yedi", userId, petId, description);
    }

    @PostMapping("/walk")
    public ResponseEntity<Activity> walk(@RequestParam Long petId,
                                         @RequestHeader("X-User-Id") String userId,
                                         @RequestBody(required = false) String description) {
        return createTypedActivity(ActivityType.WALK, "Yürüyüş yaptı", userId, petId, description);
    }

    @PostMapping("/play")
    public ResponseEntity<Activity> play(@RequestParam Long petId,
                                         @RequestHeader("X-User-Id") String userId,
                                         @RequestBody(required = false) String description) {
        return createTypedActivity(ActivityType.PLAY, "Oyun oynadı", userId, petId, description);
    }

    @PostMapping("/sleep")
    public ResponseEntity<Activity> sleep(@RequestParam Long petId,
                                          @RequestHeader("X-User-Id") String userId,
                                          @RequestBody(required = false) String description) {
        return createTypedActivity(ActivityType.SLEEP, "Uyudu", userId, petId, description);
    }

    @PostMapping("/bath")
    public ResponseEntity<Activity> bath(@RequestParam Long petId,
                                         @RequestHeader("X-User-Id") String userId,
                                         @RequestBody(required = false) String description) {
        return createTypedActivity(ActivityType.BATH, "Banyo yaptı", userId, petId, description);
    }
}
