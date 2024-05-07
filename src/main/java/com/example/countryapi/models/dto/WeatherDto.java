package com.example.countryapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeatherDto {
    private String country_name;
    private String capital;
    private double wind_speed;
    private double wind_degrees;
    private double temp;
    private double humidity;
}
