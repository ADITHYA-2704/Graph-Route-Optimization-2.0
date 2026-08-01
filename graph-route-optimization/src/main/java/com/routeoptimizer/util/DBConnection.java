package com.routeoptimizer.util;

import com.routeoptimizer.exception.DatabaseConnectionException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();

            if (input != null) {
                prop.load(input);
                url = prop.getProperty("db.url");
                username = prop.getProperty("db.username");
                password = prop.getProperty("db.password");
                driver = prop.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            } else {
                // Fallback default set to port 3307
                url = "jdbc:mysql://localhost:3307/route_optimizer_db";
                username = "root";
                password = "adhi3961";
                driver = "com.mysql.cj.jdbc.Driver";
            }

            Class.forName(driver);
        } catch (Exception e) {
            System.err.println("Database driver or properties configuration error: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if (url == null) {
            throw new DatabaseConnectionException("Database connection details are not initialized.");
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static void shutdown() {
        // Optional cleanup
    }
}