package com.routeoptimizer.algorithm;

import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.model.PathResult;

/**
 * Strategy interface implemented by every route-finding algorithm
 * (Dijkstra, A*, Bellman-Ford, ...). Keeping algorithms behind a common
 * interface lets the service layer swap implementations without changing
 * any calling code (Strategy design pattern).
 */
public interface ShortestPathAlgorithm {

    /**
     * Computes the shortest path between two cities in the given graph.
     *
     * @param graph       the graph to search
     * @param source      starting city
     * @param destination target city
     * @return a {@link PathResult} containing the shortest distance and the
     *         full ordered path, or a result with {@code reachable = false}
     *         if no path exists
     */
    PathResult findShortestPath(Graph graph, String source, String destination);
}
