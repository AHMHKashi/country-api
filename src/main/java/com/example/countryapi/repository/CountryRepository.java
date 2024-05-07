package com.example.countryapi.repository;

import com.example.countryapi.models.Country;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Repository
public class CountryRepository {
    @Value("${config.countryInfoAPI}")
    private String CountryAPI;

    private final RestTemplate restTemplate;

    public CountryRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Country> getCountryByName(final String name) {
        var result = restTemplate.getForObject(CountryAPI + "?name=" + name, Country[].class);
        if (result != null && result.length > 0) {
            return Optional.of(result[0]);
        } else {
            return Optional.empty();
        }
    }
}
