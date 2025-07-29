package com.petverse.service;

import com.petverse.model.Activity;

public interface ActivityService {
    Activity createActivity(Activity activity, String userId);
}