package com.routeoptimizer.controller;

import com.routeoptimizer.service.GraphService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/graph")
@CrossOrigin(origins = "*")
public class GraphDataController {

    private final GraphService graphService;

    public GraphDataController(GraphService graphService) {
        this.graphService = graphService;
    }

    /**
     * POST /api/graph/cities
     * Payload: { "cityName": "Hyderabad" }
     */
    @PostMapping("/cities")
    public ResponseEntity<?> addCity(@RequestBody Map<String, String> payload) {
        String cityName = payload.get("cityName");

        if (cityName == null || cityName.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "City name cannot be empty."));
        }

        try {
            boolean created = graphService.addCity(cityName.trim());
            if (!created) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "City already exists in the database."));
            }
            return ResponseEntity.ok(Map.of("message", "City successfully added!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to add city: " + e.getMessage()));
        }
    }

    /**
     * POST /api/graph/edges
     * Payload: { "source": "Delhi", "destination": "Jaipur", "distance": 280.5 }
     */
    @PostMapping("/edges")
    public ResponseEntity<?> addEdge(@RequestBody Map<String, Object> payload) {
        try {
            String source = (String) payload.get("source");
            String destination = (String) payload.get("destination");

            if (payload.get("distance") == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Distance is required."));
            }

            double distance = Double.parseDouble(payload.get("distance").toString());

            if (source == null || destination == null || source.trim().isEmpty() || destination.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Source and destination cities are required."));
            }

            if (source.equalsIgnoreCase(destination)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Source and destination cannot be the same city."));
            }

            if (distance <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Distance must be greater than zero."));
            }

            graphService.addEdge(source.trim(), destination.trim(), distance);
            return ResponseEntity.ok(Map.of("message", "Route connection added successfully!"));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid distance format."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to add edge: " + e.getMessage()));
        }
    }
}