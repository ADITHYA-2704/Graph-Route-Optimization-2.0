package com.routeoptimizer.exception;

/**
 * Thrown when a connection to the MySQL database cannot be established
 * or an underlying JDBC operation fails unexpectedly.
 */
public class DatabaseConnectionException extends RuntimeException {

    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}