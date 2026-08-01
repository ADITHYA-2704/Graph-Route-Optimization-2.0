package com.routeoptimizer.config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private String url;
    private String username;
    private String password;
    private String driver;

    public DatabaseConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input != null) {
                prop.load(input);
                this.url = prop.getProperty("db.url");
                this.username = prop.getProperty("db.username");
                this.password = prop.getProperty("db.password");
                this.driver = prop.getProperty("db.driver");
            } else {
                // Default fallback
                this.url = "jdbc:mysql://localhost:3307/route_optimizer_db";
                this.username = "root";
                this.password = "";
                this.driver = "com.mysql.cj.jdbc.Driver";
            }
        } catch (Exception e) {
            System.err.println("Error loading db.properties: " + e.getMessage());
        }
    }

    public String getUrl() { return url; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDriver() { return driver; }
}