package com.petverse.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActivityType {
    FEED,
    WALK,
    PLAY,
    SLEEP,
    BATH;

    @JsonCreator
    public static ActivityType from(String value) {
        try {
            return ActivityType.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Geçersiz activity type: " + value);
        }
    }
}
