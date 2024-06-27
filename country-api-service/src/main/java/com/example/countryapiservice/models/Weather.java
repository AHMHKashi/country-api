package com.example.countryapiservice.models;

import lombok.Data;

@Data
public class Weather {
    private double wind_speed;
    private double wind_degrees;
    private float temp;
    private float humidity;
    private long sunset;
    private long sunrise;
    private float min_temp;
    private float cloud_pct;
    private float feels_like;
    private float max_temp;

}
