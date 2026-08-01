/**
 * Graph data-structure package.
 * <p>
 * Contains the in-memory graph representation (adjacency list/matrix) used by the
 * algorithm layer, along with helpers to construct a {@code Graph} from domain
 * model objects ({@link com.routeoptimizer.model.Node}, {@link com.routeoptimizer.model.Edge}).
 * This package is purely structural - it knows nothing about the database or about
 * which shortest-path algorithm will consume it.
 */
package com.routeoptimizer.graph;