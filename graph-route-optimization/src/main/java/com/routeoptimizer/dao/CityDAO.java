package com.routeoptimizer.dao;

import com.routeoptimizer.model.City;
import com.routeoptimizer.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CityDAO {

    /**
     * Fetch all cities from database.
     */
    public List<City> getAllCities() throws SQLException {

        List<City> cities = new ArrayList<>();

        String sql =
                "SELECT city_id, city_name " +
                        "FROM cities " +
                        "ORDER BY city_name ASC";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {

            while (rs.next()) {

                City city = new City();
                city.setId(rs.getLong("city_id"));
                city.setName(rs.getString("city_name"));

                cities.add(city);
            }
        }

        return cities;
    }

    /**
     * Find city by city ID.
     */
    public City getCityById(long cityId)
            throws SQLException {

        String sql =
                "SELECT city_id, city_name " +
                        "FROM cities " +
                        "WHERE city_id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setLong(1, cityId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    return new City(
                            rs.getLong("city_id"),
                            rs.getString("city_name")
                    );
                }
            }
        }

        return null;
    }

    /**
     * Find city by name.
     */
    public City getCityByName(String cityName)
            throws SQLException {

        if (cityName == null ||
                cityName.trim().isEmpty()) {

            return null;
        }

        String sql =
                "SELECT city_id, city_name " +
                        "FROM cities " +
                        "WHERE LOWER(TRIM(city_name)) = LOWER(TRIM(?)) " +
                        "LIMIT 1";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setString(
                    1,
                    cityName.trim()
            );

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    return new City(
                            rs.getLong("city_id"),
                            rs.getString("city_name")
                    );
                }
            }
        }

        return null;
    }

    /**
     * Insert one city.
     */
    public boolean insertCity(String cityName) {

        if (cityName == null ||
                cityName.trim().isEmpty()) {

            return false;
        }

        String sql =
                "INSERT IGNORE INTO cities (city_name) " +
                        "VALUES (?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            pstmt.setString(
                    1,
                    cityName.trim()
            );

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error inserting city: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Insert multiple cities efficiently.
     */
    public int insertCitiesBatch(
            Collection<String> cityNames) {

        if (cityNames == null ||
                cityNames.isEmpty()) {

            return 0;
        }

        String sql =
                "INSERT IGNORE INTO cities (city_name) " +
                        "VALUES (?)";

        int totalInserted = 0;

        try (
                Connection conn =
                        DBConnection.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(sql)
        ) {

            conn.setAutoCommit(false);

            for (String cityName : cityNames) {

                if (cityName == null ||
                        cityName.trim().isEmpty()) {

                    continue;
                }

                pstmt.setString(
                        1,
                        cityName.trim()
                );

                pstmt.addBatch();
            }

            int[] results =
                    pstmt.executeBatch();

            for (int result : results) {

                if (result > 0) {
                    totalInserted += result;
                }
            }

            conn.commit();

        } catch (SQLException e) {

            System.err.println(
                    "Error inserting cities: "
                            + e.getMessage()
            );
        }

        return totalInserted;
    }
}