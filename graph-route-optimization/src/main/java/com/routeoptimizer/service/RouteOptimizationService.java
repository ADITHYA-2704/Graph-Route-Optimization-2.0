    package com.routeoptimizer.service;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

    @Service
    public class RouteOptimizationService {

        @Autowired
        private GraphService graphService;

        public List<String> findShortestPath(String source, String destination, String algorithm) {
            if (source == null || destination == null) {
                return new ArrayList<>();
            }

            return graphService.findShortestPath(source, destination, algorithm);
        }

        public double calculatePathDistance(List<String> path) {
            if (path == null || path.size() < 2) {
                return 0.0;
            }

            double totalDistance = 0.0;

            for (int i = 0; i < path.size() - 1; i++) {
                String u = path.get(i);
                String v = path.get(i + 1);
                totalDistance += graphService.getDistanceBetween(u, v);
            }

            return totalDistance;
        }

        public int getVisitedNodesCount() {
            return graphService.getVisitedNodesCount();
        }
    }