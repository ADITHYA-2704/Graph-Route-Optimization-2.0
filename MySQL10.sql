USE route_optimizer_db;

-- Disable Safe Update Mode temporarily for this session
SET SQL_SAFE_UPDATES = 0;

-- 1. Delete foreign key links in the routes table first
DELETE FROM routes 
WHERE source_city_id IN (SELECT city_id FROM cities WHERE city_name IN ('NewYork', 'Boston'))
   OR destination_city_id IN (SELECT city_id FROM cities WHERE city_name IN ('NewYork', 'Boston'));

-- 2. Delete the non-Indian city records
DELETE FROM cities 
WHERE city_name IN ('NewYork', 'Boston');

-- Re-enable Safe Update Mode
SET SQL_SAFE_UPDATES = 1;