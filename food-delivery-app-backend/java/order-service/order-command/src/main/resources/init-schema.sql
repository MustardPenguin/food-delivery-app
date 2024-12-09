
DROP SCHEMA IF EXISTS order_command CASCADE;

CREATE SCHEMA order_command;

SET search_path TO order_command;

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM('PENDING_PAYMENT', 'PAID', 'CANCELED', 'CANCELLING');

CREATE TABLE order_command.orders (
    order_id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    payment_id UUID,
    customer_id UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    ordered_at TIMESTAMP NOT NULL,
    address VARCHAR(255) NOT NULL,
    order_status order_status NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    error_message VARCHAR(1024)
);

CREATE TABLE order_command.order_items (
    order_item_id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,

    order_id UUID NOT NULL,
    FOREIGN KEY (order_id) REFERENCES order_command.orders(order_id)
);

CREATE TABLE order_command.order_created_events (
    event_id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_command.order_updated_events (
    event_id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);