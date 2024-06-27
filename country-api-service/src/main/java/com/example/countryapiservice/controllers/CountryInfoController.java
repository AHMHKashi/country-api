package com.example.countryapiservice.controllers;

import com.example.countryapiservice.models.Country;
import com.example.countryapiservice.models.dto.WeatherDto;
import com.example.countryapiservice.models.dto.CountryListDto;
import com.example.countryapiservice.services.CountryInfoService;
import com.example.countryapiservice.services.WeatherInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryInfoController {
    private final CountryInfoService countryInfoService;
    private final WeatherInfoService weatherInfoService;

    @GetMapping("")
    public CountryListDto getAllCountries() {
        return countryInfoService.getCountriesInfoList();
    }

    @GetMapping("/{name}")
    public Country getCountryInfo(@PathVariable("name") String name) {
        var country = countryInfoService.getCountryInfoByName(name);
        if (country.isPresent()) {
            return country.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no country with this name");
        }
    }

    @GetMapping("/{name}/weather")
    public WeatherDto getWeatherInfo(@PathVariable("name") String name) {
        var weatherDto = weatherInfoService.getWeatherInfoByCountryName(name);
        if (weatherDto.isPresent()) {
            return weatherDto.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "there is no country with this name");
        }
    }


}
