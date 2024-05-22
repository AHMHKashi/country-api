package com.example.countryapi.repository;

import com.example.countryapi.models.Country;
import com.example.countryapi.models.dto.CountriesListApiResponseDto;
import com.example.countryapi.models.dto.CountryListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Repository
public class CountryRepository {
    @Value("${config.countryInfoAPI}")
    private String CountryAPI;

    @Value("${config.countriesListAPI}")
    private String CountriesListAPI;

    private final RestTemplate restTemplate;
    private final RestTemplate ninjasTemplate;

    @Autowired
    public CountryRepository(RestTemplate restTemplate, @Qualifier("ninjasRestTemplate") RestTemplate ninjasTemplate) {
        this.restTemplate = restTemplate;
        this.ninjasTemplate = ninjasTemplate;
    }

    @Cacheable(value = "countryInfos", key = "#name")
    public Optional<Country> getCountryByName(final String name) {
        var result = ninjasTemplate.getForObject(CountryAPI + "?name=" + name, Country[].class);
        if (result != null && result.length > 0) {
            return Optional.of(result[0]);
        } else {
            return Optional.empty();
        }
    }

    @Cacheable(value = "countriesList")
    public CountryListDto.CountryName[] getCountriesList() {
        var url = CountriesListAPI + "/info?returns=name";
        var result = restTemplate.getForObject(url, CountriesListApiResponseDto.class);
        return result != null ? result.data : new CountryListDto.CountryName[0];
    }
}
