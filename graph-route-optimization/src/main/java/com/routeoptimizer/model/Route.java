package com.routeoptimizer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_city_id", nullable = false)
    private City sourceCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_city_id", nullable = false)
    private City destinationCity;

    @Column(name = "distance", nullable = false)
    private double distance;

    public Route() {}

    public Route(City sourceCity, City destinationCity, double distance) {
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.distance = distance;
    }

    public Route(Long id, City sourceCity, City destinationCity, double distance) {
        this.id = id;
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(City sourceCity) {
        this.sourceCity = sourceCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}