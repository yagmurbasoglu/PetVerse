package com.petverse.client;

import com.petverse.dto.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "weatherservice")
public interface WeatherClient {

    @GetMapping("/weather/{city}")
    WeatherResponse getWeather(@PathVariable("city") String city);
}
