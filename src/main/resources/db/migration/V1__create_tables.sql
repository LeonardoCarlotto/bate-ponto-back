CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL
);

CREATE TABLE register (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    data_time TIMESTAMP NOT NULL,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_register_user
        FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE register_audit (
    id BIGSERIAL PRIMARY KEY,
    register_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    old_data TIMESTAMP NOT NULL,
    new_data TIMESTAMP NOT NULL,
    observation TEXT,
    edited_at TIMESTAMP NOT NULL,
    edited_by_user_id BIGINT
);
