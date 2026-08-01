package com.routeoptimizer.model;

import java.util.Collections;
import java.util.List;

/**
 * Holds the result of a pathfinding algorithm run.
 */
public class PathResult {

    private final List<String> path;
    private final double totalDistance;

    public PathResult(List<String> path, double totalDistance) {
        this.path = (path != null) ? Collections.unmodifiableList(path) : Collections.emptyList();
        this.totalDistance = Math.max(0.0, totalDistance);
    }

    public List<String> getPath() {
        return path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public boolean isPathFound() {
        return !path.isEmpty();
    }

    // Added to resolve the error in ConsoleUI.java
    public boolean isReachable() {
        return !path.isEmpty();
    }
}