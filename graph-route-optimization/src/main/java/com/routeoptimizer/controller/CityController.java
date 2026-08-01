package com.routeoptimizer.controller;

import com.routeoptimizer.model.City;
import com.routeoptimizer.repository.CityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@CrossOrigin(origins = "*")
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
}