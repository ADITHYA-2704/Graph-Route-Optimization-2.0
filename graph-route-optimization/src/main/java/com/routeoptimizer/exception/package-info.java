/**
 * Custom exception package.
 * <p>
 * Defines application-specific exception types so that failures in different
 * layers (database access, graph construction, route computation) can be
 * caught and handled distinctly and meaningfully, instead of leaking raw
 * exceptions like {@code SQLException} up to the UI layer.
 */
package com.routeoptimizer.exception;