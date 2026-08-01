/**
 * Root package of the Graph-Based Route Optimization System.
 * <p>
 * Contains the application entry point ({@code Main}) only. All functional
 * concerns are organized into the sub-packages below, following a standard
 * layered architecture:
 * <ul>
 *   <li>{@link com.routeoptimizer.config}    - application & database configuration</li>
 *   <li>{@link com.routeoptimizer.model}     - domain entities (POJOs)</li>
 *   <li>{@link com.routeoptimizer.graph}     - graph data-structure representation</li>
 *   <li>{@link com.routeoptimizer.algorithm} - shortest-path / route algorithms</li>
 *   <li>{@link com.routeoptimizer.dao}       - JDBC data access objects</li>
 *   <li>{@link com.routeoptimizer.service}   - business/service layer</li>
 *   <li>{@link com.routeoptimizer.exception} - custom checked/unchecked exceptions</li>
 *   <li>{@link com.routeoptimizer.util}      - cross-cutting utility/helper classes</li>
 *   <li>{@link com.routeoptimizer.ui}        - console/CLI presentation layer</li>
 * </ul>
 */
package com.routeoptimizer;