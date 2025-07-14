package com.petverse.controller;

import com.petverse.model.Activity;
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


}
