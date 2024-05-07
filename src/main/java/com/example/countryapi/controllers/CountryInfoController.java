package com.example.countryapi.controllers;

import com.example.countryapi.models.Country;
import com.example.countryapi.models.dto.CountryListDto;
import com.example.countryapi.services.CountryInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/countries")
public class CountryInfoController {
    private final CountryInfoService countryInfoService;

    @Autowired
    public CountryInfoController(CountryInfoService countryInfoService) {
        this.countryInfoService = countryInfoService;
    }

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
}
