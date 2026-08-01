package com.routeoptimizer;

import com.routeoptimizer.ui.ConsoleUI;
import com.routeoptimizer.util.DBConnection;

/**
 * Main application entry point for the Graph-Based Route Optimization System.
 */
public class Main {

    public static void main(String[] args) {
        // Safe resource cleanup on application shutdown (e.g. Ctrl+C)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DBConnection.shutdown();
            } catch (Exception ignored) {
                // Ignore cleanup exceptions during forced JVM shutdown
            }
        }));

        try {
            // Hand off execution entirely to the UI controller
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (Exception e) {
            System.err.println("\n[FATAL ERROR] System terminated unexpectedly: " + e.getMessage());
            e.printStackTrace();
        }
    }
}