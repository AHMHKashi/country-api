package com.example.countryapi.models.dto;

import lombok.Data;

@Data
public class CountriesListApiResponseDto {
    public String error;
    public String msg;
    public CountryListDto.CountryName[] data;
}
