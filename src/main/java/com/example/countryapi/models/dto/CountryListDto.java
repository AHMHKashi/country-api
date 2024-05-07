package com.example.countryapi.models.dto;

import lombok.Data;

import java.util.List;

@Data
public class CountryListDto {
    public CountryName[] countries;
    public int count;

    public CountryListDto(List<CountryName> countries) {
        this.countries = countries.toArray(CountryName[]::new);
        this.count = this.countries.length;
    }

    @Data
    public static class CountryName {
        public String name;
    }
}

