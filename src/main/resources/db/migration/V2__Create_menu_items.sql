CREATE TABLE menu_items
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255)   NOT NULL,
    category VARCHAR(255)   NOT NULL,
    price    DECIMAL(10, 2) NOT NULL
);