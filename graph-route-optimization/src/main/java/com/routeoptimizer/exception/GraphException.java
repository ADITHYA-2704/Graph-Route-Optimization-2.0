package com.routeoptimizer.exception;

/**
 * Thrown when an error occurs while building or operating on the in-memory graph
 * (e.g. an operation is attempted on a city that hasn't been added yet).
 */
public class GraphException extends RuntimeException {

    public GraphException(String message) {
        super(message);
    }

    public GraphException(String message, Throwable cause) {
        super(message, cause);
    }
}
