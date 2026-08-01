package com.routeoptimizer.ui;

import com.routeoptimizer.algorithm.BreadthFirstSearch;
import com.routeoptimizer.algorithm.DepthFirstSearch;
import com.routeoptimizer.algorithm.DijkstraAlgorithm;
import com.routeoptimizer.dao.CityDAO;
import com.routeoptimizer.dao.RouteDAO;
import com.routeoptimizer.graph.GraphEdge;
import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.model.City;
import com.routeoptimizer.model.PathResult;
import com.routeoptimizer.model.Route;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Console User Interface for the
 * Graph-Based Route Optimization System.
 *
 * Responsibilities:
 *
 * 1. Load cities and routes from MySQL.
 * 2. Build an in-memory graph.
 * 3. Run Dijkstra, BFS and DFS algorithms.
 * 4. Add and remove cities/routes.
 * 5. Save graph data back to MySQL.
 */
public class ConsoleUI {

    private final Scanner scanner;

    private final Graph graph;
    private final CityDAO cityDAO;
    private final RouteDAO routeDAO;

    private final DijkstraAlgorithm dijkstraAlgorithm;
    private final BreadthFirstSearch breadthFirstSearch;
    private final DepthFirstSearch depthFirstSearch;


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    /**
     * Creates the Console UI.
     */
    public ConsoleUI() {

        scanner = new Scanner(System.in);

        graph = new Graph();

        cityDAO = new CityDAO();
        routeDAO = new RouteDAO();

        dijkstraAlgorithm = new DijkstraAlgorithm();
        breadthFirstSearch = new BreadthFirstSearch();
        depthFirstSearch = new DepthFirstSearch();
    }


    // ============================================================
    // APPLICATION START
    // ============================================================

    /**
     * Starts the console application.
     */
    public void start() {

        printHeader();

        /*
         * Load cities and routes from database
         * when application starts.
         */
        loadDataFromDatabase();

        boolean running = true;

        while (running) {

            printMenu();

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1":
                    addCity();
                    break;

                case "2":
                    addRoute();
                    break;

                case "3":
                    removeRoute();
                    break;

                case "4":
                    displayGraph();
                    break;

                case "5":
                    findShortestPath();
                    break;

                case "6":
                    runBFS();
                    break;

                case "7":
                    runDFS();
                    break;

                case "8":
                    saveToDatabase();
                    break;

                case "9":
                    refreshFromDatabase();
                    break;

                case "10":
                    showStatistics();
                    break;

                case "0":

                    running = false;

                    System.out.println();
                    System.out.println(
                            "Exiting application..."
                    );

                    break;

                default:

                    System.out.println();
                    System.out.println(
                            "Invalid option."
                    );

                    System.out.println(
                            "Please select a valid menu option."
                    );
            }
        }

        scanner.close();
    }


    // ============================================================
    // DATABASE - LOAD
    // ============================================================

    /**
     * Loads cities and routes from MySQL
     * into the in-memory graph.
     */
    private void loadDataFromDatabase() {

        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       LOADING DATA FROM DATABASE"
        );

        System.out.println(
                "=============================================="
        );

        try {

            /*
             * Clear old graph data first.
             */
            graph.clear();


            // ----------------------------------------------------
            // STEP 1: LOAD CITIES
            // ----------------------------------------------------

            List<City> cities =
                    cityDAO.getAllCities();

            if (cities == null) {

                cities = new ArrayList<>();
            }

            int validCities = 0;

            for (City city : cities) {

                if (!isValidCity(city)) {

                    continue;
                }

                String cityName =
                        city.getCityName().trim();

                /*
                 * IMPORTANT:
                 *
                 * Your Graph class should have:
                 *
                 * addCity(String cityName)
                 */
                graph.addCity(cityName);

                validCities++;
            }


            // ----------------------------------------------------
            // STEP 2: LOAD ROUTES
            // ----------------------------------------------------

            List<Route> routes =
                    routeDAO.getAllRoutes();

            if (routes == null) {

                routes = new ArrayList<>();
            }

            int validRoutes = 0;

            for (Route route : routes) {

                if (!isValidRoute(route)) {

                    continue;
                }

                City sourceCity =
                        route.getSourceCity();

                City destinationCity =
                        route.getDestinationCity();

                String source =
                        sourceCity
                                .getCityName()
                                .trim();

                String destination =
                        destinationCity
                                .getCityName()
                                .trim();

                double distance =
                        route.getDistance();


                /*
                 * IMPORTANT:
                 *
                 * We use addRoute(), not addRoad().
                 *
                 * This fixes your screenshot error:
                 *
                 * cannot find symbol
                 * method addRoad(...)
                 */
                graph.addRoute(
                        source,
                        destination,
                        distance,
                        false
                );

                validRoutes++;
            }


            // ----------------------------------------------------
            // STEP 3: DISPLAY RESULT
            // ----------------------------------------------------

            System.out.println();

            System.out.println(
                    "Cities loaded from database : "
                            + validCities
            );

            System.out.println(
                    "Routes loaded from database : "
                            + validRoutes
            );

            System.out.println();

            System.out.println(
                    "Graph loaded successfully."
            );

            System.out.println(
                    "Cities in graph : "
                            + graph.getCities().size()
            );

            System.out.println(
                    "Routes in graph : "
                            + graph.getAllRoutes().size()
            );

        } catch (SQLException e) {

            System.err.println();

            System.err.println(
                    "DATABASE ERROR WHILE LOADING DATA"
            );

            System.err.println(
                    "Reason: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.err.println();

            System.err.println(
                    "ERROR WHILE LOADING GRAPH"
            );

            System.err.println(
                    "Reason: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // DATABASE - REFRESH
    // ============================================================

    /**
     * Clears the current graph and reloads
     * fresh data from MySQL.
     */
    private void refreshFromDatabase() {

        System.out.println();

        System.out.println(
                "----- REFRESHING DATABASE DATA -----"
        );

        loadDataFromDatabase();

        System.out.println();

        System.out.println(
                "Graph refresh completed."
        );
    }


    // ============================================================
    // DATABASE - SAVE
    // ============================================================

    /**
     * Saves current graph data to MySQL.
     *
     * Process:
     *
     * 1. Save cities.
     * 2. Reload cities from database.
     * 3. Create city lookup map.
     * 4. Convert Graph Edge objects into Route objects.
     * 5. Insert routes using RouteDAO.
     */
    private void saveToDatabase() {

        System.out.println();

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       SAVING GRAPH TO DATABASE"
        );

        System.out.println(
                "=============================================="
        );


        try {

            // ----------------------------------------------------
            // STEP 1: SAVE CITIES
            // ----------------------------------------------------

            // ----------------------------------------------------
// STEP 1: SAVE CITIES
// ----------------------------------------------------

            Set<String> graphCities =
                    graph.getCities();

            if (graphCities == null ||
                    graphCities.isEmpty()) {

                System.out.println(
                        "No cities found in graph."
                );

                return;
            }

            int citiesInserted =
                    cityDAO.insertCitiesBatch(
                            new ArrayList<>(graphCities)
                    );

            System.out.println();

            System.out.println(
                    "Cities inserted : "
                            + citiesInserted
            );


            // ----------------------------------------------------
            // STEP 2: RELOAD DATABASE CITIES
            // ----------------------------------------------------

            List<City> databaseCities =
                    cityDAO.getAllCities();

            if (databaseCities == null ||
                    databaseCities.isEmpty()) {

                System.out.println(
                        "No cities available in database."
                );

                return;
            }


            // ----------------------------------------------------
            // STEP 3: CREATE CITY LOOKUP MAP
            // ----------------------------------------------------

            Map<String, City> cityMap =
                    createCityLookupMap(
                            databaseCities
                    );

            if (cityMap.isEmpty()) {

                System.out.println(
                        "No valid cities found in database."
                );

                return;
            }


            // ----------------------------------------------------
            // STEP 4: CONVERT GRAPH EDGES TO ROUTES
            // ----------------------------------------------------

            List<Route> routesToSave =
                    convertGraphEdgesToRoutes(
                            cityMap
                    );

            if (routesToSave.isEmpty()) {

                System.out.println();

                System.out.println(
                        "No valid routes found to save."
                );

                return;
            }


            // ----------------------------------------------------
// STEP 5: INSERT ROUTES
// ----------------------------------------------------

            int routesInserted = 0;
            try {
                routesInserted = routeDAO.insertRoutesBatch(routesToSave);
            } catch (SQLException e) {
                System.err.println("Failed to insert routes: " + e.getMessage());
            }


            // ----------------------------------------------------
            // STEP 6: DISPLAY RESULT
            // ----------------------------------------------------

            System.out.println();

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "DATABASE SYNCHRONIZATION COMPLETED"
            );

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "Cities inserted : "
                            + citiesInserted
            );

            System.out.println(
                    "Routes prepared : "
                            + routesToSave.size()
            );

            System.out.println(
                    "Routes inserted : "
                            + routesInserted
            );

            System.out.println(
                    "=============================================="
            );

        } catch (SQLException e) {

            System.err.println();

            System.err.println(
                    "DATABASE ERROR"
            );

            System.err.println(
                    "Unable to save graph."
            );

            System.err.println(
                    "Reason: "
                            + e.getMessage()
            );

        } catch (Exception e) {

            System.err.println();

            System.err.println(
                    "UNEXPECTED ERROR"
            );

            System.err.println(
                    "Reason: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // CITY LOOKUP
    // ============================================================

    /**
     * Creates a case-insensitive city lookup map.
     *
     * Key:
     *     Normalized city name.
     *
     * Value:
     *     Actual City object from database.
     */
    private Map<String, City> createCityLookupMap(
            List<City> cities
    ) {

        Map<String, City> cityMap =
                new HashMap<>();

        if (cities == null) {

            return cityMap;
        }

        for (City city : cities) {

            if (!isValidCity(city)) {

                continue;
            }

            String key =
                    normalizeCityName(
                            city.getCityName()
                    );

            cityMap.put(
                    key,
                    city
            );
        }

        return cityMap;
    }


    // ============================================================
    // EDGE -> ROUTE CONVERSION
    // ============================================================

    /**
     * Converts graph Edge objects into
     * database Route objects.
     */
    private List<Route> convertGraphEdgesToRoutes(
            Map<String, City> cityMap
    ) {

        List<Route> routes =
                new ArrayList<>();

        if (cityMap == null ||
                cityMap.isEmpty()) {

            return routes;
        }

        List<GraphEdge> edges =
                graph.getAllRoutes();

        if (edges == null ||
                edges.isEmpty()) {

            return routes;
        }


        for (GraphEdge edge : edges) {

            if (edge == null) {

                continue;
            }


            // ----------------------------------------------------
            // GET EDGE DATA
            // ----------------------------------------------------

            String sourceName =
                    edge.getSource();

            String destinationName =
                    edge.getDestination();

            double distance =
                    edge.getDistance();


            // ----------------------------------------------------
            // VALIDATE CITY NAMES
            // ----------------------------------------------------

            if (sourceName == null ||
                    destinationName == null) {

                continue;
            }

            if (sourceName.trim().isEmpty() ||
                    destinationName.trim().isEmpty()) {

                continue;
            }


            // ----------------------------------------------------
            // VALIDATE DISTANCE
            // ----------------------------------------------------

            if (!isValidDistance(distance)) {

                System.err.println(
                        "Skipping invalid route: "
                                + sourceName
                                + " -> "
                                + destinationName
                );

                continue;
            }


            // ----------------------------------------------------
            // FIND DATABASE CITY OBJECTS
            // ----------------------------------------------------

            City sourceCity =
                    cityMap.get(
                            normalizeCityName(
                                    sourceName
                            )
                    );

            City destinationCity =
                    cityMap.get(
                            normalizeCityName(
                                    destinationName
                            )
                    );


            // ----------------------------------------------------
            // CHECK SOURCE CITY
            // ----------------------------------------------------

            if (sourceCity == null) {

                System.err.println(
                        "Skipping route: "
                                + sourceName
                                + " -> "
                                + destinationName
                                + " | Source city not found."
                );

                continue;
            }


            // ----------------------------------------------------
            // CHECK DESTINATION CITY
            // ----------------------------------------------------

            if (destinationCity == null) {

                System.err.println(
                        "Skipping route: "
                                + sourceName
                                + " -> "
                                + destinationName
                                + " | Destination city not found."
                );

                continue;
            }


            // ----------------------------------------------------
            // CREATE ROUTE
            // ----------------------------------------------------

            Route route =
                    new Route(
                            sourceCity,
                            destinationCity,
                            distance
                    );

            routes.add(route);
        }

        return routes;
    }


    // ============================================================
    // CITY OPERATIONS
    // ============================================================

    /**
     * Adds a city to the in-memory graph.
     */
    private void addCity() {

        System.out.println();

        System.out.println(
                "----- ADD CITY -----"
        );

        String cityName =
                readCityName(
                        "Enter city name: "
                );


        if (cityExists(cityName)) {

            System.out.println(
                    "City already exists."
            );

            return;
        }


        boolean added =
                graph.addCity(cityName);


        if (added) {

            System.out.println(
                    "City added successfully: "
                            + cityName
            );

        } else {

            System.out.println(
                    "Unable to add city."
            );
        }
    }


    // ============================================================
    // ROUTE OPERATIONS
    // ============================================================

    /**
     * Adds a route to the graph.
     */
    private void addRoute() {

        System.out.println();

        System.out.println(
                "----- ADD ROUTE -----"
        );


        String source =
                readCityName(
                        "Enter source city: "
                );


        String destination =
                readCityName(
                        "Enter destination city: "
                );


        if (source.equalsIgnoreCase(destination)) {

            System.out.println(
                    "Source and destination cannot be the same."
            );

            return;
        }


        double distance =
                readDistance();


        boolean bidirectional =
                readYesNo(
                        "Is this a two-way route? (y/n): "
                );


        /*
         * Make sure both cities exist.
         */
        graph.addCity(source);

        graph.addCity(destination);


        /*
         * IMPORTANT:
         *
         * Your Graph class must have:
         *
         * addRoute(
         *     String source,
         *     String destination,
         *     double distance,
         *     boolean bidirectional
         * )
         */
        graph.addRoute(
                source,
                destination,
                distance,
                bidirectional
        );


        System.out.println();

        System.out.println(
                "Route added successfully."
        );


        System.out.printf(
                "%s -> %s : %.2f km%n",
                source,
                destination,
                distance
        );


        if (bidirectional) {

            System.out.printf(
                    "%s -> %s : %.2f km%n",
                    destination,
                    source,
                    distance
            );
        }
    }


    /**
     * Removes a route from graph.
     */
    private void removeRoute() {

        System.out.println();

        System.out.println(
                "----- REMOVE ROUTE -----"
        );


        String source =
                readCityName(
                        "Enter source city: "
                );


        String destination =
                readCityName(
                        "Enter destination city: "
                );


        boolean removed =
                graph.removeRoute(
                        source,
                        destination
                );


        if (removed) {

            System.out.println(
                    "Route removed successfully."
            );

        } else {

            System.out.println(
                    "Route not found."
            );
        }
    }


    // ============================================================
    // GRAPH DISPLAY
    // ============================================================

    /**
     * Displays the graph.
     */
    private void displayGraph() {

        System.out.println();

        System.out.println(
                "----- CURRENT GRAPH -----"
        );


        if (graph.getCities().isEmpty()) {

            System.out.println(
                    "Graph is empty."
            );

            return;
        }


        graph.displayGraph();
    }


    /**
     * Displays graph statistics.
     */
    private void showStatistics() {

        System.out.println();

        System.out.println(
                "----- GRAPH STATISTICS -----"
        );


        int cityCount =
                graph.getCities().size();


        int routeCount =
                graph.getAllRoutes().size();


        System.out.println(
                "Total Cities : "
                        + cityCount
        );


        System.out.println(
                "Total Routes : "
                        + routeCount
        );


        if (cityCount > 0) {

            double average =
                    (double) routeCount
                            / cityCount;


            System.out.printf(
                    "Average Routes per City : %.2f%n",
                    average
            );
        }
    }


    // ============================================================
    // DIJKSTRA
    // ============================================================

    /**
     * Finds the shortest path using Dijkstra.
     */
    private void findShortestPath() {

        System.out.println();

        System.out.println(
                "----- FIND SHORTEST PATH -----"
        );


        if (graph.getCities().isEmpty()) {

            System.out.println(
                    "Graph is empty."
            );

            return;
        }


        String source =
                readCityName(
                        "Enter start city: "
                );


        String destination =
                readCityName(
                        "Enter destination city: "
                );


        if (!cityExists(source)) {

            System.out.println(
                    "Source city does not exist."
            );

            return;
        }


        if (!cityExists(destination)) {

            System.out.println(
                    "Destination city does not exist."
            );

            return;
        }


        try {

            PathResult result =
                    dijkstraAlgorithm.findShortestPath(
                            graph,
                            source,
                            destination
                    );


            if (result == null || !result.isPathFound()) {

                System.out.println(
                        "No route found."
                );

                return;
            }


            System.out.println();

            System.out.println(
                    "========== SHORTEST PATH =========="
            );


            System.out.println(
                    "Source      : "
                            + source
            );


            System.out.println(
                    "Destination : "
                            + destination
            );


            System.out.println(
                    "Path        : "
                            + String.join(
                            " -> ",
                            result.getPath()
                    )
            );


            System.out.printf(
                    "Distance    : %.2f km%n",
                    result.getTotalDistance()
            );


            System.out.println(
                    "==================================="
            );


        } catch (Exception e) {

            System.err.println(
                    "Dijkstra error: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // BFS
    // ============================================================

    /**
     * Runs Breadth-First Search.
     */
    private void runBFS() {

        System.out.println();

        System.out.println(
                "----- BREADTH-FIRST SEARCH -----"
        );


        String startCity =
                readCityName(
                        "Enter starting city: "
                );


        if (!cityExists(startCity)) {

            System.out.println(
                    "City not found."
            );

            return;
        }


        try {

            List<String> result =
                    breadthFirstSearch.traverse(
                            graph,
                            startCity
                    );


            if (result == null ||
                    result.isEmpty()) {

                System.out.println(
                        "No cities visited."
                );

                return;
            }


            System.out.println();

            System.out.println(
                    "BFS Traversal:"
            );


            System.out.println(
                    String.join(
                            " -> ",
                            result
                    )
            );


        } catch (Exception e) {

            System.err.println(
                    "BFS error: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // DFS
    // ============================================================

    /**
     * Runs Depth-First Search.
     */
    private void runDFS() {

        System.out.println();

        System.out.println(
                "----- DEPTH-FIRST SEARCH -----"
        );


        String startCity =
                readCityName(
                        "Enter starting city: "
                );


        if (!cityExists(startCity)) {

            System.out.println(
                    "City not found."
            );

            return;
        }


        try {

            List<String> result =
                    depthFirstSearch.traverse(
                            graph,
                            startCity
                    );


            if (result == null ||
                    result.isEmpty()) {

                System.out.println(
                        "No cities visited."
                );

                return;
            }


            System.out.println();

            System.out.println(
                    "DFS Traversal:"
            );


            System.out.println(
                    String.join(
                            " -> ",
                            result
                    )
            );


        } catch (Exception e) {

            System.err.println(
                    "DFS error: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // VALIDATION
    // ============================================================

    /**
     * Checks whether a city exists in graph.
     */
    private boolean cityExists(
            String cityName
    ) {

        if (cityName == null ||
                cityName.trim().isEmpty()) {

            return false;
        }


        String normalizedInput =
                normalizeCityName(
                        cityName
                );


        for (String city :
                graph.getCities()) {

            if (city != null &&
                    normalizeCityName(city)
                            .equals(normalizedInput)) {

                return true;
            }
        }


        return false;
    }


    /**
     * Validates City object.
     */
    private boolean isValidCity(
            City city
    ) {

        return city != null
                && city.getCityName() != null
                && !city.getCityName()
                .trim()
                .isEmpty();
    }


    /**
     * Validates Route object.
     */
    private boolean isValidRoute(
            Route route
    ) {

        if (route == null) {

            return false;
        }


        City sourceCity =
                route.getSourceCity();


        City destinationCity =
                route.getDestinationCity();


        if (!isValidCity(sourceCity) ||
                !isValidCity(destinationCity)) {

            return false;
        }


        return isValidDistance(
                route.getDistance()
        );
    }


    /**
     * Validates distance.
     */
    private boolean isValidDistance(
            double distance
    ) {

        return Double.isFinite(distance)
                && distance > 0;
    }


    /**
     * Normalizes city names.
     */
    private String normalizeCityName(
            String cityName
    ) {

        if (cityName == null) {

            return "";
        }


        return cityName
                .trim()
                .toLowerCase(Locale.ROOT);
    }


    // ============================================================
    // INPUT
    // ============================================================

    /**
     * Reads city name.
     */
    private String readCityName(
            String prompt
    ) {

        while (true) {

            System.out.print(prompt);


            String input =
                    scanner.nextLine()
                            .trim();


            if (!input.isEmpty()) {

                return input;
            }


            System.out.println(
                    "City name cannot be empty."
            );
        }
    }


    /**
     * Reads route distance.
     */
    private double readDistance() {

        while (true) {

            System.out.print(
                    "Enter distance (km): "
            );


            String input =
                    scanner.nextLine()
                            .trim();


            try {

                double distance =
                        Double.parseDouble(
                                input
                        );


                if (isValidDistance(distance)) {

                    return distance;
                }


                System.out.println(
                        "Distance must be greater than 0."
                );


            } catch (
                    NumberFormatException e
            ) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }


    /**
     * Reads yes/no input.
     */
    private boolean readYesNo(
            String prompt
    ) {

        while (true) {

            System.out.print(prompt);


            String input =
                    scanner.nextLine()
                            .trim()
                            .toLowerCase(
                                    Locale.ROOT
                            );


            if (input.equals("y") ||
                    input.equals("yes")) {

                return true;
            }


            if (input.equals("n") ||
                    input.equals("no")) {

                return false;
            }


            System.out.println(
                    "Please enter y/yes or n/no."
            );
        }
    }


    // ============================================================
    // UI
    // ============================================================

    /**
     * Prints application header.
     */
    private void printHeader() {

        System.out.println();

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "     GRAPH-BASED ROUTE OPTIMIZATION SYSTEM"
        );

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "        Java + Graph + MySQL + JDBC"
        );

        System.out.println(
                "=================================================="
        );
    }


    /**
     * Prints main menu.
     */
    private void printMenu() {

        System.out.println();

        System.out.println(
                "---------------- MAIN MENU ----------------"
        );

        System.out.println(
                "1. Add City"
        );

        System.out.println(
                "2. Add Route"
        );

        System.out.println(
                "3. Remove Route"
        );

        System.out.println(
                "4. Display Graph"
        );

        System.out.println(
                "5. Find Shortest Path (Dijkstra)"
        );

        System.out.println(
                "6. BFS Traversal"
        );

        System.out.println(
                "7. DFS Traversal"
        );

        System.out.println(
                "8. Save Changes to Database"
        );

        System.out.println(
                "9. Reload Data from Database"
        );

        System.out.println(
                "10. Graph Statistics"
        );

        System.out.println(
                "0. Exit"
        );

        System.out.println(
                "-------------------------------------------"
        );

        System.out.print(
                "Enter your choice: "
        );
    }
}