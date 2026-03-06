-- Initial schema for users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_address TEXT,
    phone_number VARCHAR(20),
    status VARCHAR(50) NOT NULL DEFAULT 'RESTRICTED',
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'RESTRICTED')),
    CONSTRAINT chk_role CHECK (role IN ('USER', 'ADMIN'))
);

-- Create index on email for faster lookups
CREATE INDEX idx_users_email ON users(user_email);

-- Insert default admin user
INSERT INTO users (user_name, user_email, password, user_address, phone_number, status, role)
VALUES (
    'Admin',
    'admin@gmail.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Admin Office',
    '0123456789',
    'ACTIVE',
    'ADMIN'
);
