package com.petverse.weatherservice.service;

import com.petverse.weatherservice.model.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getAnkaraWeather() {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=39.93&longitude=32.86&current_weather=true";
        return restTemplate.getForObject(url, WeatherResponse.class);
    }
}
