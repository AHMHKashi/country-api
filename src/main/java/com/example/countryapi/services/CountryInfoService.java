package com.example.countryapi.services;

import com.example.countryapi.models.Country;
import com.example.countryapi.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CountryInfoService {
    private final CountryRepository countryRepository;

    @Autowired
    public CountryInfoService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Optional<Country> getCountryInfoByName(String name){
        var country = countryRepository.getCountryByName(name);
        return country;
    }
}
