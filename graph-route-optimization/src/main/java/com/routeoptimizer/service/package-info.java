/**
 * Service (business logic) package.
 * <p>
 * Orchestrates the other layers: pulls data via the {@code dao} package, builds a
 * {@link com.routeoptimizer.graph.Graph} via the {@code graph} package, runs the
 * chosen strategy from the {@code algorithm} package, and returns results to the
 * {@code ui} layer. This is where use-case-level business logic lives; DAOs and
 * algorithms themselves stay generic and reusable.
 */
package com.routeoptimizer.service;