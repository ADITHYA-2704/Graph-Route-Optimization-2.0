-- 1. Update password for local root user
ALTER USER 'root'@'localhost' IDENTIFIED BY 'adhi3961';

-- 2. Create and grant privileges for wildcard host root user (if accessing externally)
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'adhi3961';
GRANT ALL PRIVILEGES ON route_optimizer_db.* TO 'root'@'%';

-- 3. Apply privilege changes
FLUSH PRIVILEGES;