package com.routeoptimizer.service;

import com.routeoptimizer.dao.CityDAO;
import com.routeoptimizer.dao.RouteDAO;
import com.routeoptimizer.graph.Graph;
import com.routeoptimizer.model.City;
import com.routeoptimizer.model.Route;

import java.util.List;

public class RouteService {
    private final CityDAO cityDAO = new CityDAO();
    private final RouteDAO routeDAO = new RouteDAO();
    private final Graph graph = new Graph();

    public void initializeGraphFromDatabase() {
        System.out.println("Loading database records into graph...");

        try {
            // 1. Fetch cities explicitly from CityDAO
            List<City> cities = cityDAO.getAllCities();
            for (City city : cities) {
                graph.addCity(city.getCityName());
            }

            // 2. Fetch routes explicitly from RouteDAO
            List<Route> routes = routeDAO.getAllRoutes();
            for (Route route : routes) {
                graph.addRoute(
                        route.getSourceCity().getCityName(),
                        route.getDestinationCity().getCityName(),
                        route.getDistance()
                );
            }

            // 3. Extract exact size counters using graph.getCities()
            int totalCities = graph.getCities().size();
            int totalRoutes = routes.size();

            System.out.println("Successfully loaded " + totalCities + " cities and " + totalRoutes + " routes from database.");

        } catch (Exception e) {
            System.err.println("Warning: Could not load data from database: " + e.getMessage());
        }
    }

    public Graph getGraph() {
        return graph;
    }
}