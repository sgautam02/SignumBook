package com.signumapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.signumapp.entity.Country;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
