package com.petverse.weatherservice.controller;

import com.petverse.dto.WeatherResponse;
import com.petverse.weatherservice.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/ankara")
    public WeatherResponse.CurrentWeather getAnkaraWeather() {
        return weatherService.getAnkaraWeather().getCurrentWeather();
    }

    // 🔧 FeignClient için destek endpoint
    @GetMapping("/{city}")
    public WeatherResponse getWeatherByCity(@PathVariable String city) {
        if (city.equalsIgnoreCase("ankara")) {
            return weatherService.getAnkaraWeather();
        }
        throw new IllegalArgumentException("Desteklenmeyen şehir: " + city);
    }
}

