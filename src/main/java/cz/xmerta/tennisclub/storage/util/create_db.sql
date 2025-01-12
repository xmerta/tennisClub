CREATE TABLE surface_types
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    price_per_minute DOUBLE       NOT NULL
);

CREATE TABLE users
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    name         VARCHAR(255) NOT NULL
);

CREATE TABLE courts
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    surface_type_id  BIGINT       NOT NULL,
    CONSTRAINT fk_surface_type
        FOREIGN KEY (surface_type_id)
            REFERENCES surface_types (id)
);

CREATE TABLE reservations
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    court_id   BIGINT       NOT NULL,
    start_time TIMESTAMP    NOT NULL,
    end_time   TIMESTAMP    NOT NULL,
    game_type  VARCHAR(255) NOT NULL,
    price      DOUBLE       NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),
    CONSTRAINT fk_court
        FOREIGN KEY (court_id)
            REFERENCES courts (id)
);
