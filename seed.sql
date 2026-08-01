USE route_optimizer_db;

-- 1. Disable Foreign Key checks for fast cleanup
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE routes;
TRUNCATE TABLE cities;
SET FOREIGN_KEY_CHECKS = 1;

-- 2. Populate All 35 Unique State/UT Capital Cities
INSERT INTO cities (city_name) VALUES
('Amaravati'), ('Itanagar'), ('Dispur'), ('Patna'), ('Raipur'),
('Panaji'), ('Gandhinagar'), ('Chandigarh'), ('Shimla'), ('Ranchi'),
('Bengaluru'), ('Thiruvananthapuram'), ('Bhopal'), ('Mumbai'), ('Imphal'),
('Shillong'), ('Aizawl'), ('Kohima'), ('Bhubaneswar'), ('Jaipur'),
('Gangtok'), ('Chennai'), ('Hyderabad'), ('Agartala'), ('Lucknow'),
('Dehradun'), ('Kolkata'), ('Port Blair'), ('Daman'), ('New Delhi'),
('Srinagar'), ('Jammu'), ('Leh'), ('Kavaratti'), ('Puducherry');

-- 3. Populate Corridors (Bidirectional to ensure every city has outgoing routes)
INSERT IGNORE INTO routes (source_city_id, destination_city_id, distance)
SELECT src.city_id, dst.city_id, r.distance
FROM (
    -- Northern Region
    SELECT 'New Delhi' AS src_name, 'Chandigarh' AS dst_name, 245.00 AS distance UNION ALL
    SELECT 'Chandigarh', 'New Delhi', 245.00 UNION ALL
    SELECT 'New Delhi', 'Jaipur', 270.00 UNION ALL
    SELECT 'Jaipur', 'New Delhi', 270.00 UNION ALL
    SELECT 'New Delhi', 'Lucknow', 530.00 UNION ALL
    SELECT 'Lucknow', 'New Delhi', 530.00 UNION ALL
    SELECT 'New Delhi', 'Dehradun', 250.00 UNION ALL
    SELECT 'Dehradun', 'New Delhi', 250.00 UNION ALL
    SELECT 'Chandigarh', 'Shimla', 110.00 UNION ALL
    SELECT 'Shimla', 'Chandigarh', 110.00 UNION ALL
    SELECT 'Chandigarh', 'Jammu', 340.00 UNION ALL
    SELECT 'Jammu', 'Chandigarh', 340.00 UNION ALL
    SELECT 'Jammu', 'Srinagar', 260.00 UNION ALL
    SELECT 'Srinagar', 'Jammu', 260.00 UNION ALL
    SELECT 'Srinagar', 'Leh', 420.00 UNION ALL
    SELECT 'Leh', 'Srinagar', 420.00 UNION ALL

    -- Western & Central Region
    SELECT 'Jaipur', 'Gandhinagar', 620.00 UNION ALL
    SELECT 'Gandhinagar', 'Jaipur', 620.00 UNION ALL
    SELECT 'Gandhinagar', 'Daman', 360.00 UNION ALL
    SELECT 'Daman', 'Gandhinagar', 360.00 UNION ALL
    SELECT 'Daman', 'Mumbai', 170.00 UNION ALL
    SELECT 'Mumbai', 'Daman', 170.00 UNION ALL
    SELECT 'New Delhi', 'Bhopal', 780.00 UNION ALL
    SELECT 'Bhopal', 'New Delhi', 780.00 UNION ALL
    SELECT 'Bhopal', 'Mumbai', 770.00 UNION ALL
    SELECT 'Mumbai', 'Bhopal', 770.00 UNION ALL
    SELECT 'Bhopal', 'Raipur', 630.00 UNION ALL
    SELECT 'Raipur', 'Bhopal', 630.00 UNION ALL
    SELECT 'Mumbai', 'Panaji', 590.00 UNION ALL
    SELECT 'Panaji', 'Mumbai', 590.00 UNION ALL

    -- Eastern Region
    SELECT 'Lucknow', 'Patna', 530.00 UNION ALL
    SELECT 'Patna', 'Lucknow', 530.00 UNION ALL
    SELECT 'Patna', 'Ranchi', 330.00 UNION ALL
    SELECT 'Ranchi', 'Patna', 330.00 UNION ALL
    SELECT 'Patna', 'Kolkata', 580.00 UNION ALL
    SELECT 'Kolkata', 'Patna', 580.00 UNION ALL
    SELECT 'Ranchi', 'Bhubaneswar', 460.00 UNION ALL
    SELECT 'Bhubaneswar', 'Ranchi', 460.00 UNION ALL
    SELECT 'Ranchi', 'Raipur', 580.00 UNION ALL
    SELECT 'Raipur', 'Ranchi', 580.00 UNION ALL
    SELECT 'Kolkata', 'Bhubaneswar', 440.00 UNION ALL
    SELECT 'Bhubaneswar', 'Kolkata', 440.00 UNION ALL

    -- North-Eastern Region
    SELECT 'Kolkata', 'Gangtok', 670.00 UNION ALL
    SELECT 'Gangtok', 'Kolkata', 670.00 UNION ALL
    SELECT 'Kolkata', 'Dispur', 1000.00 UNION ALL
    SELECT 'Dispur', 'Kolkata', 1000.00 UNION ALL
    SELECT 'Dispur', 'Shillong', 100.00 UNION ALL
    SELECT 'Shillong', 'Dispur', 100.00 UNION ALL
    SELECT 'Dispur', 'Itanagar', 320.00 UNION ALL
    SELECT 'Itanagar', 'Dispur', 320.00 UNION ALL
    SELECT 'Dispur', 'Kohima', 350.00 UNION ALL
    SELECT 'Kohima', 'Dispur', 350.00 UNION ALL
    SELECT 'Kohima', 'Imphal', 140.00 UNION ALL
    SELECT 'Imphal', 'Kohima', 140.00 UNION ALL
    SELECT 'Imphal', 'Aizawl', 370.00 UNION ALL
    SELECT 'Aizawl', 'Imphal', 370.00 UNION ALL
    SELECT 'Dispur', 'Agartala', 550.00 UNION ALL
    SELECT 'Agartala', 'Dispur', 550.00 UNION ALL

    -- Southern Region
    SELECT 'Mumbai', 'Hyderabad', 710.00 UNION ALL
    SELECT 'Hyderabad', 'Mumbai', 710.00 UNION ALL
    SELECT 'Mumbai', 'Bengaluru', 980.00 UNION ALL
    SELECT 'Bengaluru', 'Mumbai', 980.00 UNION ALL
    SELECT 'Hyderabad', 'Bengaluru', 570.00 UNION ALL
    SELECT 'Bengaluru', 'Hyderabad', 570.00 UNION ALL
    SELECT 'Hyderabad', 'Amaravati', 270.00 UNION ALL
    SELECT 'Amaravati', 'Hyderabad', 270.00 UNION ALL
    SELECT 'Bhubaneswar', 'Amaravati', 780.00 UNION ALL
    SELECT 'Amaravati', 'Bhubaneswar', 780.00 UNION ALL
    SELECT 'Amaravati', 'Chennai', 430.00 UNION ALL
    SELECT 'Chennai', 'Amaravati', 430.00 UNION ALL
    SELECT 'Bengaluru', 'Chennai', 350.00 UNION ALL
    SELECT 'Chennai', 'Bengaluru', 350.00 UNION ALL
    SELECT 'Bengaluru', 'Thiruvananthapuram', 680.00 UNION ALL
    SELECT 'Thiruvananthapuram', 'Bengaluru', 680.00 UNION ALL
    SELECT 'Chennai', 'Puducherry', 150.00 UNION ALL
    SELECT 'Puducherry', 'Chennai', 150.00 UNION ALL
    SELECT 'Chennai', 'Thiruvananthapuram', 700.00 UNION ALL
    SELECT 'Thiruvananthapuram', 'Chennai', 700.00 UNION ALL

    -- Island UT Connections (Air/Sea Corridors)
    SELECT 'Kolkata', 'Port Blair', 1300.00 UNION ALL
    SELECT 'Port Blair', 'Kolkata', 1300.00 UNION ALL
    SELECT 'Chennai', 'Port Blair', 1370.00 UNION ALL
    SELECT 'Port Blair', 'Chennai', 1370.00 UNION ALL
    SELECT 'Thiruvananthapuram', 'Kavaratti', 400.00 UNION ALL
    SELECT 'Kavaratti', 'Thiruvananthapuram', 400.00
) AS r
JOIN cities src ON src.city_name = r.src_name
JOIN cities dst ON dst.city_name = r.dst_name;