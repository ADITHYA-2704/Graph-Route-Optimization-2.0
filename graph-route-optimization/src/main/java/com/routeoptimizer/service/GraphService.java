package com.routeoptimizer.service;

import com.routeoptimizer.algorithm.*;
import com.routeoptimizer.dao.CityDAO;
import com.routeoptimizer.dao.RouteDAO;
import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.City;
import com.routeoptimizer.model.PathResult;
import com.routeoptimizer.model.Route;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphService {

    private final Graph memoryGraph = new Graph();
    private final CityDAO cityDAO = new CityDAO();
    private final RouteDAO routeDAO = new RouteDAO();
    private int visitedNodesCount = 0;

    /**
     * Auto-loads cities and bidirectional routes from DB into graph on startup.
     */
    @PostConstruct
    public void initGraphFromDatabase() {
        try {
            memoryGraph.clear();
            List<City> cities = cityDAO.getAllCities();
            if (cities != null) {
                for (City c : cities) {
                    memoryGraph.addCity(c.getCityName());
                }
            }

            List<Route> routes = routeDAO.getAllRoutes();
            if (routes != null) {
                for (Route r : routes) {
                    memoryGraph.addRoute(
                            r.getSourceCity().getCityName(),
                            r.getDestinationCity().getCityName(),
                            r.getDistance(),
                            true
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Database auto-load warning: " + e.getMessage());
        }
    }

    /**
     * Finds shortest/traversal path using selected algorithm strategy.
     */
    public List<String> findShortestPath(String source, String destination, String algorithmType) {
        ShortestPathAlgorithm algorithm;

        if (algorithmType != null) {
            String algo = algorithmType.toLowerCase();

            if (algo.contains("bellman")) {
                algorithm = new BellmanFordAlgorithm();
            } else if (algo.contains("a-star") || algo.contains("astar")) {
                algorithm = new AStarAlgorithm();
            } else if (algo.contains("bfs") || algo.contains("breadth")) {
                algorithm = new BreadthFirstSearch();
            } else if (algo.contains("dfs") || algo.contains("depth")) {
                algorithm = new DepthFirstSearch();
            } else {
                algorithm = new DijkstraAlgorithm();
            }
        } else {
            algorithm = new DijkstraAlgorithm();
        }

        PathResult result = algorithm.findShortestPath(memoryGraph, source, destination);

        this.visitedNodesCount = (result != null && result.isPathFound()) ? result.getPath().size() : 0;

        return (result != null) ? result.getPath() : Collections.emptyList();
    }

    public double calculatePathDistance(List<String> path) {
        if (path == null || path.size() < 2) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += getDistanceBetween(path.get(i), path.get(i + 1));
        }

        return totalDistance;
    }

    public double getDistanceBetween(String source, String destination) {
        if (source == null || destination == null) return 0.0;

        List<GraphEdge> neighbors = memoryGraph.getNeighbors(source);
        if (neighbors != null) {
            for (GraphEdge edge : neighbors) {
                if (edge.getDestination().equalsIgnoreCase(destination.trim())) {
                    return edge.getDistance();
                }
            }
        }
        return 0.0;
    }

    public boolean addCity(String cityName) {
        return memoryGraph.addCity(cityName);
    }

    public void addEdge(String source, String destination, double distance) {
        memoryGraph.addRoute(source, destination, distance, true);
    }

    public int getVisitedNodesCount() {
        return visitedNodesCount;
    }

    public Graph getMemoryGraph() {
        return memoryGraph;
    }
}