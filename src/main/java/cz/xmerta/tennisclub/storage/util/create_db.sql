CREATE TABLE surface_types
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    price_multiplier DOUBLE       NOT NULL
);