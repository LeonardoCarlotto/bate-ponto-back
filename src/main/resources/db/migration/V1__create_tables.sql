CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE register (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    data_time TIMESTAMP NOT NULL,
    type VARCHAR(20) NOT NULL,
    edited BOOLEAN DEFAULT false,
    observation TEXT,
    CONSTRAINT fk_register_user
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE register_audit (
    id BIGSERIAL PRIMARY KEY,
    register_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    old_data TEXT,
    new_data TEXT,
    observation TEXT,
    edited_at TIMESTAMP NOT NULL,
    edited_by_user_id BIGINT
);
