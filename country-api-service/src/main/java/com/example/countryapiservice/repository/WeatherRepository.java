package com.example.countryapiservice.repository;

import com.example.countryapiservice.models.Geocoding;
import com.example.countryapiservice.models.Weather;
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
        var coordinatesList = ninjasTemplate.getForObject("https://api.api-ninjas.com/v1/geocoding?city=" + cityName, Geocoding[].class);
        if(coordinatesList == null || coordinatesList.length == 0) {
            return Optional.empty();
        }
        var coordinates = coordinatesList[0];
        var result = ninjasTemplate.getForObject(WeatherAPI + "?lat=" + coordinates.getLatitude() + "&lon=" + coordinates.getLongitude(), Weather.class);
        if (result != null) {
            return Optional.of(result);
        }
        return Optional.empty();
    }
}
