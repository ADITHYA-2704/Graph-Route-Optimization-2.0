package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.PathResult;

import java.util.*;

public class BreadthFirstSearch implements ShortestPathAlgorithm {

    /**
     * Traverses the graph level-by-level starting from the given start city.
     * Used by ConsoleUI.
     */
    public List<String> traverse(Graph graph, String startCity) {
        List<String> visitedOrder = new ArrayList<>();
        if (graph == null || startCity == null || !graph.getCities().contains(startCity)) {
            return visitedOrder;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(startCity);
        visited.add(startCity);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            visitedOrder.add(current);

            List<GraphEdge> neighbors = graph.getNeighbors(current);
            if (neighbors != null) {
                for (GraphEdge edge : neighbors) {
                    String neighbor = edge.getDestination();
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return visitedOrder;
    }

    /**
     * Pathfinding implementation matching ShortestPathAlgorithm interface.
     * Used by Web REST API / GraphService.
     */
    @Override
    public PathResult findShortestPath(Graph graph, String source, String destination) {
        if (graph == null || source == null || destination == null) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        Queue<String> queue = new LinkedList<>();
        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        queue.add(source);
        visited.add(source);

        boolean found = false;

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equalsIgnoreCase(destination)) {
                found = true;
                break;
            }

            List<GraphEdge> neighbors = graph.getNeighbors(current);
            if (neighbors != null) {
                for (GraphEdge edge : neighbors) {
                    String neighbor = edge.getDestination();
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parentMap.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }

        if (!found) {
            return new PathResult(Collections.emptyList(), 0.0);
        }

        List<String> path = new ArrayList<>();
        String curr = destination;
        while (curr != null) {
            path.add(0, curr);
            curr = parentMap.get(curr);
        }

        return new PathResult(path, 0.0);
    }
}