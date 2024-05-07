package com.example.countryapi.services;

import com.example.countryapi.models.dto.WeatherDto;
import com.example.countryapi.repository.CountryRepository;
import com.example.countryapi.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherInfoService {

    private final WeatherRepository weatherRepository;

    private final CountryRepository countryRepository;

    @Autowired
    public WeatherInfoService(WeatherRepository weatherRepository, CountryRepository countryRepository) {
        this.weatherRepository = weatherRepository;
        this.countryRepository = countryRepository;
    }


    public Optional<WeatherDto> getWeatherInfoByCountryName(String countryName) {
        var country = countryRepository.getCountryByName(countryName);
        if (country.isPresent()) {
            String countryCapital = country.get().getCapital();
            var optionalWeather = weatherRepository.getWeatherByCityName(countryCapital);
            if (optionalWeather.isPresent()) {
                var weather = optionalWeather.get();
                var wind_speed = weather.getWind_speed();
                var wind_degrees = weather.getWind_degrees();
                var temp = weather.getTemp();
                var humidity = weather.getHumidity();
                var weatherDto = new WeatherDto(countryName, countryCapital, wind_speed, wind_degrees, temp, humidity);
                return Optional.of(weatherDto);
            }
            return Optional.empty();
        }
        return Optional.empty();
    }


}
