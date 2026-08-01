package com.routeoptimizer.graph;

/**
 * Represents an in-memory route/edge for graph algorithms.
 */
public class GraphEdge {

    private final String source;
    private final String destination;
    private final double distance;

    public GraphEdge(String source, String destination, double distance) {
        this.source = source;
        this.destination = destination;
        this.distance = distance;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " (" + distance + " km)";
    }
}