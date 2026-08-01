package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.PathResult;

import java.util.*;

public class DepthFirstSearch implements ShortestPathAlgorithm {

    /**
     * Traverses the graph deep-first starting from startCity.
     * Used by ConsoleUI.
     */
    public List<String> traverse(Graph graph, String startCity) {
        List<String> visitedOrder = new ArrayList<>();
        if (graph == null || startCity == null || !graph.getCities().contains(startCity)) {
            return visitedOrder;
        }

        Set<String> visited = new HashSet<>();
        traverseHelper(graph, startCity, visited, visitedOrder);
        return visitedOrder;
    }

    private void traverseHelper(Graph graph, String current, Set<String> visited, List<String> visitedOrder) {
        visited.add(current);
        visitedOrder.add(current);

        List<GraphEdge> neighbors = graph.getNeighbors(current);
        if (neighbors != null) {
            for (GraphEdge edge : neighbors) {
                String neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    traverseHelper(graph, neighbor, visited, visitedOrder);
                }
            }
        }
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

        List<String> path = new ArrayList<>();
        Set<String> visited = new HashSet<>();

        if (dfsPathHelper(graph, source, destination, visited, path)) {
            return new PathResult(path, 0.0);
        }

        return new PathResult(Collections.emptyList(), 0.0);
    }

    private boolean dfsPathHelper(Graph graph, String current, String destination, Set<String> visited, List<String> path) {
        visited.add(current);
        path.add(current);

        if (current.equalsIgnoreCase(destination)) {
            return true;
        }

        List<GraphEdge> neighbors = graph.getNeighbors(current);
        if (neighbors != null) {
            for (GraphEdge edge : neighbors) {
                String neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    if (dfsPathHelper(graph, neighbor, destination, visited, path)) {
                        return true;
                    }
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }
}