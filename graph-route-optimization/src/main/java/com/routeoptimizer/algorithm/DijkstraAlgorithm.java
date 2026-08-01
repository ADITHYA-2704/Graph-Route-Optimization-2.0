package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.PathResult;

import java.util.*;

public class DijkstraAlgorithm implements ShortestPathAlgorithm {

    @Override
    public PathResult findShortestPath(Graph graph, String source, String destination) {
        if (graph == null || source == null || destination == null) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        String start = source.trim();
        String end = destination.trim();

        if (start.equalsIgnoreCase(end)) {
            return new PathResult(List.of(start), 0.0);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(NodeDistance::getDistance));
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String city : graph.getCities()) {
            distances.put(city, Double.MAX_VALUE);
        }

        distances.put(start, 0.0);
        pq.add(new NodeDistance(start, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String u = current.getCity();

            if (!visited.add(u)) continue;
            if (u.equalsIgnoreCase(end)) break; // Reached target

            for (GraphEdge edge : graph.getNeighbors(u)) {
                String v = edge.getDestination();
                double weight = edge.getDistance();

                if (visited.contains(v)) continue;

                double newDist = distances.getOrDefault(u, Double.MAX_VALUE) + weight;
                if (newDist < distances.getOrDefault(v, Double.MAX_VALUE)) {
                    distances.put(v, newDist);
                    predecessors.put(v, u);
                    pq.add(new NodeDistance(v, newDist));
                }
            }
        }

        double finalDistance = distances.getOrDefault(end, Double.MAX_VALUE);
        if (finalDistance == Double.MAX_VALUE) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        // Reconstruct path
        LinkedList<String> path = new LinkedList<>();
        String curr = end;
        while (curr != null) {
            path.addFirst(curr);
            curr = predecessors.get(curr);
        }

        return new PathResult(path, finalDistance);
    }

    private static class NodeDistance {
        private final String city;
        private final double distance;

        public NodeDistance(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }

        public String getCity() { return city; }
        public double getDistance() { return distance; }
    }
}