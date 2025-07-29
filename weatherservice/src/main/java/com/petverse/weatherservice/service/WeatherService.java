package com.petverse.weatherservice.service;

import com.petverse.dto.WeatherResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    public WeatherResponse getWeather(double latitude, double longitude) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.open-meteo.com/v1/forecast")
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current_weather", true)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, WeatherResponse.class);
    }

    public WeatherResponse getAnkaraWeather() {
        return getWeather(39.9208, 32.8541);
    }
}