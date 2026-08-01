package com.routeoptimizer.repository;

import com.routeoptimizer.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(String name);

    // Add this line to fix the error:
    boolean existsByName(String name);
}