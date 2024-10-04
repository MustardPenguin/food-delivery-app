
DROP SCHEMA IF EXISTS order_command CASCADE;

CREATE SCHEMA order_command;

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM('PENDING_PAYMENT', 'PAID');

CREATE TABLE order_command.orders (
    order_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    ordered_at TIMESTAMP NOT NULL,
    address VARCHAR(255) NOT NULL,
    order_status order_status NOT NULL
);

CREATE TABLE order_command.order_items (
    order_item_id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,

    order_id UUID NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_command.orders(order_id)
);

CREATE TABLE order_command.order_created_events (
    event_id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);