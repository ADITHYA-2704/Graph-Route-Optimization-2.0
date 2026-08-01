CREATE DATABASE IF NOT EXISTS route_optimizer_db;
USE route_optimizer_db;

-- Clear old tables if rebuilding structure
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS cities;

-- 1. Cities Table
CREATE TABLE cities (
                        city_id INT AUTO_INCREMENT PRIMARY KEY,
                        city_name VARCHAR(100) NOT NULL UNIQUE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Routes Table (Graph Edges)
CREATE TABLE routes (
                        route_id INT AUTO_INCREMENT PRIMARY KEY,
                        source_city_id INT NOT NULL,
                        destination_city_id INT NOT NULL,
                        distance DECIMAL(10, 2) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_routes_source FOREIGN KEY (source_city_id)
                            REFERENCES cities(city_id) ON DELETE CASCADE,
                        CONSTRAINT fk_routes_destination FOREIGN KEY (destination_city_id)
                            REFERENCES cities(city_id) ON DELETE CASCADE,
                        CONSTRAINT uq_routes_source_destination UNIQUE (source_city_id, destination_city_id)
);