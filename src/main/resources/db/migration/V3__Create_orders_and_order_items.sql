CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    waiter_id  BIGINT    NOT NULL,
    order_time TIMESTAMP NOT NULL,
    FOREIGN KEY (waiter_id) REFERENCES users (id)
);

CREATE TABLE order_items
(
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity     INT    NOT NULL,
    comment      VARCHAR(255),
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
);
