package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.PathResult;

import java.util.*;

public class BellmanFordAlgorithm implements ShortestPathAlgorithm {

    @Override
    public PathResult findShortestPath(Graph graph, String source, String destination) {
        if (graph == null || source == null || destination == null) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        String start = source.trim();
        String end = destination.trim();

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();

        for (String city : graph.getCities()) {
            distances.put(city, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);

        int nodeCount = graph.getCities().size();
        List<GraphEdge> allEdges = graph.getAllRoutes();

        // Relax edges V - 1 times
        for (int i = 1; i < nodeCount; i++) {
            boolean updated = false;
            for (GraphEdge edge : allEdges) {
                String u = edge.getSource();
                String v = edge.getDestination();
                double weight = edge.getDistance();

                if (u == null || distances.get(u) == Double.MAX_VALUE) continue;

                if (distances.get(u) + weight < distances.getOrDefault(v, Double.MAX_VALUE)) {
                    distances.put(v, distances.get(u) + weight);
                    predecessors.put(v, u);
                    updated = true;
                }
            }
            if (!updated) break;
        }

        double finalDistance = distances.getOrDefault(end, Double.MAX_VALUE);
        if (finalDistance == Double.MAX_VALUE) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        LinkedList<String> path = new LinkedList<>();
        String curr = end;
        while (curr != null) {
            path.addFirst(curr);
            curr = predecessors.get(curr);
        }

        return new PathResult(path, finalDistance);
    }
}