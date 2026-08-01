package com.routeoptimizer.controller;

import com.routeoptimizer.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; // <--- Added missing import

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
public class RouteController {

    private final GraphService graphService;

    public RouteController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/find-route")
    public ResponseEntity<Map<String, Object>> findShortestRoute(@RequestBody RouteRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request == null || request.getSource() == null || request.getDestination() == null) {
            response.put("error", "Invalid source or destination city.");
            return ResponseEntity.badRequest().body(response);
        }

        List<String> path = graphService.findShortestPath(
                request.getSource(),
                request.getDestination(),
                request.getAlgorithm()
        );

        double totalDistance = graphService.calculatePathDistance(path);

        response.put("path", path);
        response.put("totalDistance", totalDistance);
        response.put("visitedNodes", graphService.getVisitedNodesCount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cities")
    public ResponseEntity<Set<String>> getAllCities() {
        return ResponseEntity.ok(graphService.getMemoryGraph().getCities());
    }

    public static class RouteRequest {
        private String source;
        private String destination;
        private String algorithm;

        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }

        public String getDestination() { return destination; }
        public void setDestination(String destination) { this.destination = destination; }

        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    }
}