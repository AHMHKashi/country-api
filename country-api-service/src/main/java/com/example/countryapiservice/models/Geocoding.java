package com.example.countryapiservice.models;

import lombok.Data;

@Data
public class Geocoding {
        private String name;
        private double latitude;
        private double longitude;
        private String country;
}
