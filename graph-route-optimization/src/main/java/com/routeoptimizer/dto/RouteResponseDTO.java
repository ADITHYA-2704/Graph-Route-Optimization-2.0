package com.routeoptimizer.dto;

import java.util.List;

public class RouteResponseDTO {
    private List<String> path;
    private double shortestDistance;
    private String algorithmUsed;
    private double executionTimeMs;
    private int nodesVisited;

    public RouteResponseDTO() {}

    public RouteResponseDTO(List<String> path, double shortestDistance, String algorithmUsed, double executionTimeMs, int nodesVisited) {
        this.path = path;
        this.shortestDistance = shortestDistance;
        this.algorithmUsed = algorithmUsed;
        this.executionTimeMs = executionTimeMs;
        this.nodesVisited = nodesVisited;
    }

    public List<String> getPath() { return path; }
    public void setPath(List<String> path) { this.path = path; }

    public double getShortestDistance() { return shortestDistance; }
    public void setShortestDistance(double shortestDistance) { this.shortestDistance = shortestDistance; }

    public String getAlgorithmUsed() { return algorithmUsed; }
    public void setAlgorithmUsed(String algorithmUsed) { this.algorithmUsed = algorithmUsed; }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public int getNodesVisited() { return nodesVisited; }
    public void setNodesVisited(int nodesVisited) { this.nodesVisited = nodesVisited; }
}