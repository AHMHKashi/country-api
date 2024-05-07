package com.example.countryapi.repository;

import com.example.countryapi.models.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Repository
public class WeatherRepository {
    @Value("${config.weatherAPI}")
    private String WeatherAPI;

    private final RestTemplate restTemplate;

    public WeatherRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }




    public Optional<Weather> getWeatherByCityName(String cityName) {
        var result = restTemplate.getForObject(WeatherAPI + "?city=" + cityName, Weather.class);
        if (result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
