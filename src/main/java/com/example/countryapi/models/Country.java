package com.example.countryapi.models;

import lombok.Data;


@Data
public class Country {
    private String name;
    private String capital;
    private String iso2;
    private double population;
    private double pop_growth;
    private Currency currency;
}
