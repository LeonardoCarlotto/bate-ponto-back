-- Database initialization script
-- This script runs when MySQL container starts for the first time

USE bate_ponto;

-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type ENUM('ADMIN', 'EMPLOYEE') NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'EMPLOYEE') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Create registers table if not exists
CREATE TABLE IF NOT EXISTS registers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    entry_time DATETIME NOT NULL,
    exit_time DATETIME,
    manual BOOLEAN DEFAULT FALSE,
    edited BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_entry_time (entry_time),
    INDEX idx_manual (manual),
    INDEX idx_edited (edited)
);

-- Insert default admin user (password: admin123)
-- Password is BCrypt encoded for "admin123"
INSERT IGNORE INTO users (name, type, email, password, role, active) 
VALUES ('Admin User', 'ADMIN', 'admin@bateponto.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', TRUE);

-- Insert sample employee user (password: employee123)
-- Password is BCrypt encoded for "employee123"
INSERT IGNORE INTO users (name, type, email, password, role, active) 
VALUES ('Employee User', 'EMPLOYEE', 'employee@bateponto.com', '$2a$10$CwTycUXWue0Thq9StjUM0uJ6QGwFUvKyRHGDbe.xjjQIb8NJ5KV8O', 'EMPLOYEE', TRUE);
