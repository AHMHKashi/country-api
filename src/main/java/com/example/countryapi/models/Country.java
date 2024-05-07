package com.example.countryapi.models;

import lombok.Data;


@Data
public class Country {
    public String name;
    public String capital;
    public String iso2;
    public double population;
    public double pop_growth;
    public Currency currency;
}
