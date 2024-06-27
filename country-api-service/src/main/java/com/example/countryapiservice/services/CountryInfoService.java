package com.example.countryapiservice.services;

import com.example.countryapiservice.models.Country;
import com.example.countryapiservice.models.dto.CountryListDto;
import com.example.countryapiservice.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class CountryInfoService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryInfoService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Optional<Country> getCountryInfoByName(String name) {
        return countryRepository.getCountryByName(name);
    }

    public CountryListDto getCountriesInfoList() {
        var countries = Arrays.stream(countryRepository.getCountriesList()).toList();
        return new CountryListDto(countries);
    }
}
