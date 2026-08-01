package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.model.PathResult;

import java.util.*;

public class AStarAlgorithm implements ShortestPathAlgorithm {

    @Override
    public PathResult findShortestPath(Graph graph, String source, String destination) {
        // Standard Dijkstra fallback when geographical coordinates/heuristics aren't specified
        return new DijkstraAlgorithm().findShortestPath(graph, source, destination);
    }
}