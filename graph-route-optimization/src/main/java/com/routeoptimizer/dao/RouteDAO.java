package com.routeoptimizer.dao;

import com.routeoptimizer.model.City;
import com.routeoptimizer.model.Route;
import com.routeoptimizer.util.DBConnection;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RouteDAO {

    /**
     * Fetch all routes from database.
     */
    public List<Route> getAllRoutes() throws SQLException {
        List<Route> routes = new ArrayList<>();

        String sql =
                "SELECT " +
                        "r.route_id, " +
                        "c1.city_id AS source_id, " +
                        "c1.city_name AS source_name, " +
                        "c2.city_id AS destination_id, " +
                        "c2.city_name AS destination_name, " +
                        "r.distance " +
                        "FROM routes r " +
                        "JOIN cities c1 ON r.source_city_id = c1.city_id " +
                        "JOIN cities c2 ON r.destination_city_id = c2.city_id " +
                        "ORDER BY r.route_id";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                City sourceCity = new City(
                        rs.getLong("source_id"),
                        rs.getString("source_name")
                );

                City destinationCity = new City(
                        rs.getLong("destination_id"),
                        rs.getString("destination_name")
                );

                Route route = new Route();

                // FIX: Use setId(Long) instead of setRouteId(int)
                route.setId(rs.getLong("route_id"));
                route.setSourceCity(sourceCity);
                route.setDestinationCity(destinationCity);
                route.setDistance(rs.getDouble("distance"));

                routes.add(route);
            }
        }

        return routes;
    }

    /**
     * Insert routes in batch into MySQL.
     */
    public int insertRoutesBatch(List<Route> routes) throws SQLException {
        if (routes == null || routes.isEmpty()) {
            return 0;
        }

        String sql =
                "INSERT INTO routes (source_city_id, destination_city_id, distance) " +
                        "VALUES (?, ?, ?)";

        int totalInserted = 0;

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            conn.setAutoCommit(false);

            for (Route route : routes) {
                if (route == null || route.getSourceCity() == null || route.getDestinationCity() == null) {
                    continue;
                }

                pstmt.setLong(1, route.getSourceCity().getId());
                pstmt.setLong(2, route.getDestinationCity().getId());
                pstmt.setDouble(3, route.getDistance());

                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();

            for (int result : results) {
                if (result > 0) {
                    totalInserted += result;
                }
            }

            conn.commit();
            return totalInserted;

        } catch (SQLException e) {
            System.err.println("Error inserting routes batch: " + e.getMessage());
            throw e;
        }
    }
}