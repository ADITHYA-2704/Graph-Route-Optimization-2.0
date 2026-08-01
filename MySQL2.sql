USE route_optimizer_db;

-- 1. Clear existing table contents safely
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE routes;
TRUNCATE TABLE cities;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Populate cities first
INSERT INTO cities (city_name) VALUES
('Amaravati'), ('Itanagar'), ('Dispur'), ('Patna'), ('Raipur'),
('Panaji'), ('Gandhinagar'), ('Chandigarh'), ('Shimla'), ('Ranchi'),
('Bengaluru'), ('Thiruvananthapuram'), ('Bhopal'), ('Mumbai'), ('Imphal'),
('Shillong'), ('Aizawl'), ('Kohima'), ('Bhubaneswar'), ('Jaipur'),
('Gangtok'), ('Chennai'), ('Hyderabad'), ('Agartala'), ('Lucknow'),
('Dehradun'), ('Kolkata'), ('Port Blair'), ('Daman'), ('New Delhi'),
('Srinagar'), ('Jammu'), ('Leh'), ('Kavaratti'), ('Puducherry');

-- 3. Populate routes
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