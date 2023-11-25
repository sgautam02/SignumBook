package com.signumapp.service;

import java.util.List;

import com.signumapp.entity.Country;

public interface CountryService {
    Country getCountryById(Long id);
    Country getCountryByName(String name);
    List<Country> getCountryList();
}
