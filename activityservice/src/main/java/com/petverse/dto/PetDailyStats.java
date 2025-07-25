package com.petverse.dto;

import com.petverse.model.Activity;
import com.petverse.model.ActivityType;
import com.petverse.dto.WeatherResponse;
import lombok.Data;

import java.util.List;

@Data
public class PetDailyStats {
    private int walkCount;
    private int foodCount;
    private int playCount;
    private int runCount;
    private int sleepCount;
    private int bathCount;

    private double temperature;
    private int weatherCode;
    private double windSpeed;

    public static PetDailyStats from(List<Activity> activities, WeatherResponse weather) {
        PetDailyStats stats = new PetDailyStats();

        for (Activity a : activities) {
            ActivityType type = a.getType();
            if (type == null) continue;

            switch (type) {
                case WALK -> stats.walkCount++;
                case FEED -> stats.foodCount++;
                case PLAY -> stats.playCount++;
                case RUN -> stats.runCount++;
                case SLEEP -> stats.sleepCount++;
                case BATH -> stats.bathCount++;
            }
        }

        if (weather != null && weather.getCurrentWeather() != null) {
            stats.temperature = weather.getCurrentWeather().getTemperature();
            stats.weatherCode = weather.getCurrentWeather().getWeathercode();
            stats.windSpeed = weather.getCurrentWeather().getWindspeed();
        }

        return stats;
    }
}
