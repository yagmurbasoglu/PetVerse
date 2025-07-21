package com.petverse.weatherservice.controller;

import com.petverse.weatherservice.model.WeatherResponse;
import com.petverse.weatherservice.service.WeatherService;
import com.petverse.weatherservice.service.WeatherAdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
    private final WeatherAdviceService adviceService;

    public WeatherController(WeatherService weatherService, WeatherAdviceService adviceService) {
        this.weatherService = weatherService;
        this.adviceService = adviceService;
    }

    @GetMapping("/ankara")
    public ResponseEntity<WeatherResponse> getWeather() {
        return ResponseEntity.ok(weatherService.getAnkaraWeather());
    }

    @GetMapping("/ankara-advice")
    public ResponseEntity<String> getAdvice() {
        WeatherResponse response = weatherService.getAnkaraWeather();
        String advice = adviceService.generateAdvice(response.getCurrent_weather());
        return ResponseEntity.ok(advice);
    }
}
