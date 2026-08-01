package com.routeoptimizer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id")
    private Long id;

    @Column(name = "city_name", nullable = false, unique = true)
    private String name;

    // Convenience constructor for creating new cities without specifying ID
    public City(String name) {
        this.name = name;
    }

    // --- ALIAS METHODS FOR BACKWARD COMPATIBILITY ---
    // Kept manually so legacy code using getCityName()/setCityName() continues working seamlessly

    public String getCityName() {
        return this.name;
    }

    public void setCityName(String cityName) {
        this.name = cityName;
    }
}