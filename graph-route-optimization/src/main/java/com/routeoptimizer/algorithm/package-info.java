/**
 * Route-finding algorithm package.
 * <p>
 * Contains the {@code ShortestPathAlgorithm} strategy interface and its concrete
 * implementations (Dijkstra, A*, Bellman-Ford, etc.). Each algorithm operates on a
 * {@link com.routeoptimizer.graph.Graph} and produces a {@link com.routeoptimizer.model.Route}.
 * Keeping algorithms behind a common interface allows the service layer to swap
 * strategies without changing calling code (Strategy design pattern).
 */
package com.routeoptimizer.algorithm;