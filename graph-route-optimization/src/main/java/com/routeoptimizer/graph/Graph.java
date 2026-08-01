package com.routeoptimizer.graph;

import java.util.*;

/**
 * Represents the graph used by the Graph-Based Route Optimization System.
 * Uses an adjacency list representation with case-insensitive city lookups,
 * duplicate edge prevention, and bidirectional routing by default.
 */
public class Graph {

    /*
     * Key   -> City name (stored in case-preserved form)
     * Value -> List of outgoing edges from that city
     */
    private final Map<String, List<GraphEdge>> adjacencyList = new LinkedHashMap<>();

    // Map used to maintain exact city name casing for user display and lookups
    private final Map<String, String> normalizedCityNames = new HashMap<>();


    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public Graph() {
        // Default constructor initializing structures
    }


    // ============================================================
    // ADD CITY
    // ============================================================

    /**
     * Adds a city to the graph with case-insensitivity checks.
     *
     * @param city city name
     * @return true if city was added, false if already exists or invalid
     */
    public boolean addCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return false;
        }

        String cleanCity = city.trim();
        String key = cleanCity.toLowerCase();

        if (!normalizedCityNames.containsKey(key)) {
            normalizedCityNames.put(key, cleanCity);
            adjacencyList.put(cleanCity, new ArrayList<>());
            return true;
        }

        return false;
    }


    // ============================================================
    // ADD ROUTE / ADD EDGE
    // ============================================================

    /**
     * Adds a two-way (bidirectional) route by default.
     *
     * @param source source city
     * @param destination destination city
     * @param distance distance in kilometers
     */
    public void addRoute(String source, String destination, double distance) {
        addRoute(source, destination, distance, true); // Defaulted to true for road networks
    }

    /**
     * Alias method for addRoute to maintain compatibility with GraphService calls.
     */
    public void addEdge(String source, String destination, double distance) {
        addRoute(source, destination, distance, true);
    }

    /**
     * Alias method accepting a GraphEdge instance.
     */
    public void addEdge(GraphEdge edge) {
        if (edge != null) {
            addRoute(edge.getSource(), edge.getDestination(), edge.getDistance(), true);
        }
    }

    /**
     * Adds a route to the graph with duplicate edge handling.
     *
     * @param source source city
     * @param destination destination city
     * @param distance distance in kilometers
     * @param bidirectional true for two-way route
     */
    public void addRoute(String source, String destination, double distance, boolean bidirectional) {
        if (source == null || destination == null || distance <= 0 || !Double.isFinite(distance)) {
            return;
        }

        String cleanSource = source.trim();
        String cleanDestination = destination.trim();

        if (cleanSource.isEmpty() || cleanDestination.isEmpty() || cleanSource.equalsIgnoreCase(cleanDestination)) {
            return; // Prevent empty names or self-loops
        }

        // Ensure cities exist in graph
        addCity(cleanSource);
        addCity(cleanDestination);

        // Fetch exact canonical name stored in graph
        String canonicalSource = getCanonicalCityName(cleanSource);
        String canonicalDestination = getCanonicalCityName(cleanDestination);

        // Add edge source -> destination
        addOrUpdateDirectedEdge(canonicalSource, canonicalDestination, distance);

        // Add edge destination -> source if bidirectional
        if (bidirectional) {
            addOrUpdateDirectedEdge(canonicalDestination, canonicalSource, distance);
        }
    }

    private void addOrUpdateDirectedEdge(String src, String dst, double distance) {
        List<GraphEdge> edges = adjacencyList.get(src);

        // Prevent duplicate edges by updating distance if edge already exists
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getDestination().equalsIgnoreCase(dst)) {
                edges.set(i, new GraphEdge(src, dst, distance));
                return;
            }
        }
        edges.add(new GraphEdge(src, dst, distance));
    }


    // ============================================================
    // REMOVE ROUTE
    // ============================================================

    public boolean removeRoute(String source, String destination) {
        if (source == null || destination == null) {
            return false;
        }

        String canonicalSource = getCanonicalCityName(source.trim());
        String canonicalDestination = getCanonicalCityName(destination.trim());

        if (canonicalSource == null || !adjacencyList.containsKey(canonicalSource)) {
            return false;
        }

        List<GraphEdge> routes = adjacencyList.get(canonicalSource);
        boolean removedForward = routes.removeIf(
                edge -> edge.getDestination().equalsIgnoreCase(canonicalDestination)
        );

        // Also check and remove reverse edge if present
        if (canonicalDestination != null && adjacencyList.containsKey(canonicalDestination)) {
            adjacencyList.get(canonicalDestination).removeIf(
                    edge -> edge.getDestination().equalsIgnoreCase(canonicalSource)
            );
        }

        return removedForward;
    }


    // ============================================================
    // GETTERS & UTILITIES
    // ============================================================

    public Set<String> getCities() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    public List<GraphEdge> getNeighbors(String city) {
        if (city == null) {
            return Collections.emptyList();
        }

        String canonicalCity = getCanonicalCityName(city.trim());
        if (canonicalCity == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(
                adjacencyList.getOrDefault(canonicalCity, Collections.emptyList())
        );
    }

    public List<GraphEdge> getAllRoutes() {
        List<GraphEdge> allRoutes = new ArrayList<>();
        for (List<GraphEdge> routes : adjacencyList.values()) {
            allRoutes.addAll(routes);
        }
        return Collections.unmodifiableList(allRoutes);
    }

    public void clear() {
        adjacencyList.clear();
        normalizedCityNames.clear();
    }

    private String getCanonicalCityName(String city) {
        if (city == null) return null;
        return normalizedCityNames.get(city.trim().toLowerCase());
    }


    // ============================================================
    // DISPLAY GRAPH
    // ============================================================

    public void displayGraph() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("         NETWORK ADJACENCY LIST");
        System.out.println("========================================");

        if (adjacencyList.isEmpty()) {
            System.out.println("No cities or routes available.");
            System.out.println("========================================");
            return;
        }

        for (Map.Entry<String, List<GraphEdge>> entry : adjacencyList.entrySet()) {
            String city = entry.getKey();
            List<GraphEdge> routes = entry.getValue();

            System.out.print(city + " -> ");

            if (routes.isEmpty()) {
                System.out.println("No outgoing routes");
                continue;
            }

            for (int i = 0; i < routes.size(); i++) {
                GraphEdge edge = routes.get(i);
                System.out.print(edge.getDestination() + " (" + edge.getDistance() + " km)");

                if (i < routes.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
        System.out.println("========================================");
    }
}