package com.routeoptimizer.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1:3307/route_optimizer_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try (Connection conn = DriverManager.getConnection(url, "root", "adhi3961")) {
            System.out.println("✅ CONNECTION SUCCESSFUL TO PORT 3307!");
        } catch (Exception e) {
            System.err.println("❌ CONNECTION FAILED: " + e.getMessage());
        }
    }
}