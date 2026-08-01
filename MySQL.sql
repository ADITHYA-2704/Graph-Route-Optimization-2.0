-- 1. Select Database
USE route_optimizer_db;

-- 2. Drop tables in order to clear existing indexes and constraints
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS cities;

-- 3. Create cities table
CREATE TABLE cities (
    city_id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    CONSTRAINT uq_cities_city_name UNIQUE (city_name)
) ENGINE = InnoDB;

-- 4. Create routes table
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    source_city_id INT NOT NULL,
    destination_city_id INT NOT NULL,
    distance DECIMAL(10, 2) NOT NULL,

    CONSTRAINT fk_routes_source
        FOREIGN KEY (source_city_id) REFERENCES cities (city_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_routes_destination
        FOREIGN KEY (destination_city_id) REFERENCES cities (city_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_routes_no_self_loop
        CHECK (source_city_id <> destination_city_id),

    CONSTRAINT chk_routes_distance_positive
        CHECK (distance > 0),

    CONSTRAINT uq_routes_source_destination
        UNIQUE (source_city_id, destination_city_id)
) ENGINE = InnoDB;

-- 5. Create destination index
CREATE INDEX idx_routes_destination_city_id ON routes (destination_city_id);

-- 6. Seed Indian state capitals & union territories
INSERT INTO cities (city_name) VALUES
('Amaravati'), ('Itanagar'), ('Dispur'), ('Patna'), ('Raipur'),
('Panaji'), ('Gandhinagar'), ('Chandigarh'), ('Shimla'), ('Ranchi'),
('Bengaluru'), ('Thiruvananthapuram'), ('Bhopal'), ('Mumbai'), ('Imphal'),
('Shillong'), ('Aizawl'), ('Kohima'), ('Bhubaneswar'), ('Jaipur'),
('Gangtok'), ('Chennai'), ('Hyderabad'), ('Agartala'), ('Lucknow'),
('Dehradun'), ('Kolkata'), ('Port Blair'), ('Daman'), ('New Delhi'),
('Srinagar'), ('Jammu'), ('Leh'), ('Kavaratti'), ('Puducherry');

-- 7. Populate routes
INSERT INTO routes (source_city_id, destination_city_id, distance)
SELECT src.city_id, dst.city_id, r.distance
FROM (
    SELECT 'New Delhi' AS src_name, 'Mumbai' AS dst_name, 1400.00 AS distance UNION ALL
    SELECT 'New Delhi', 'Bengaluru', 2180.00 UNION ALL
    SELECT 'New Delhi', 'Kolkata', 1530.00 UNION ALL
    SELECT 'Mumbai', 'Bengaluru', 980.00 UNION ALL
    SELECT 'Bengaluru', 'Chennai', 620.00 UNION ALL
    SELECT 'Bengaluru', 'Hyderabad', 570.00 UNION ALL
    SELECT 'Mumbai', 'Hyderabad', 710.00 UNION ALL
    SELECT 'Kolkata', 'Chennai', 1670.00 UNION ALL
    SELECT 'New Delhi', 'Lucknow', 530.00 UNION ALL
    SELECT 'New Delhi', 'Jaipur', 270.00
) AS r
JOIN cities src ON src.city_name = r.src_name
JOIN cities dst ON dst.city_name = r.dst_name;

-- 8. Update root user password for localhost
ALTER USER 'root'@'localhost' IDENTIFIED BY 'adhi3961';
FLUSH PRIVILEGES;