package com.petverse.service;

import com.petverse.dto.WeatherResponse;
import org.springframework.stereotype.Service;

@Service
public class WeatherAdviceService {

    public String generateAdvice(WeatherResponse.CurrentWeather current) {
        double temp = current.getTemperature();
        double wind = current.getWindspeed();
        int code = current.getWeathercode();

        if (code >= 61 && code <= 65) {
            return "Hava yağmurlu, yürüyüş için uygun değil.";
        }
        if (temp >= 30) {
            return "Hava çok sıcak. Gölgelik yerleri tercih edin, şapka ve güneş kremi kullanın.";
        }
        if (temp <= 5) {
            return "Hava çok soğuk. Petinizin üşümemesi için önlem alın.";
        }
        if (wind >= 20) {
            return "Rüzgar kuvvetli. Dikkatli olun.";
        }
        return "Hava yürüyüş için uygun görünüyor.";
    }
}
