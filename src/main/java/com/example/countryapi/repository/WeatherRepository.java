package com.example.countryapi.repository;

import com.example.countryapi.models.Weather;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Repository
public class WeatherRepository {
    @Value("${config.weatherAPI}")
    private String WeatherAPI;

    private final RestTemplate ninjasTemplate;

    public WeatherRepository(@Qualifier("ninjasRestTemplate") RestTemplate ninjasTemplate) {
        this.ninjasTemplate = ninjasTemplate;
    }

    @Cacheable(value = "weatherInfos", key = "#cityName")
    public Optional<Weather> getWeatherByCityName(String cityName) {
        var result = ninjasTemplate.getForObject(WeatherAPI + "?city=" + cityName, Weather.class);
        if (result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
