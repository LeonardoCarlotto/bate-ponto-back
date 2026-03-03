-- Database initialization script for PostgreSQL
-- This script runs when PostgreSQL container starts for the first time

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('ADMIN', 'EMPLOYEE')),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'EMPLOYEE')),
    active BOOLEAN DEFAULT TRUE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);

-- Create registers table
CREATE TABLE IF NOT EXISTS registers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    manual BOOLEAN DEFAULT FALSE,
    edited BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for registers
CREATE INDEX IF NOT EXISTS idx_registers_user_id ON registers(user_id);
CREATE INDEX IF NOT EXISTS idx_registers_entry_time ON registers(entry_time);
CREATE INDEX IF NOT EXISTS idx_registers_manual ON registers(manual);
CREATE INDEX IF NOT EXISTS idx_registers_edited ON registers(edited);

-- Insert default admin user (password: admin123)
-- Password is BCrypt encoded for "admin123"
INSERT INTO users (name, type, email, password, role, active) 
VALUES ('Admin User', 'ADMIN', 'admin@bateponto.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', TRUE)
ON CONFLICT (email) DO NOTHING;

-- Insert sample employee user (password: employee123)
-- Password is BCrypt encoded for "employee123"
INSERT INTO users (name, type, email, password, role, active) 
VALUES ('Employee User', 'EMPLOYEE', 'employee@bateponto.com', '$2a$10$CwTycUXWue0Thq9StjUM0uJ6QGwFUvKyRHGDbe.xjjQIb8NJ5KV8O', 'EMPLOYEE', TRUE)
ON CONFLICT (email) DO NOTHING;
