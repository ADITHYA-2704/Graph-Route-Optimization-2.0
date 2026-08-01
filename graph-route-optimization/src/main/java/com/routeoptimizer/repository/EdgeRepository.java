package com.routeoptimizer.repository;

import com.routeoptimizer.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Route, Long> {
}