
DROP SCHEMA IF EXISTS order_command CASCADE;

CREATE SCHEMA order_command;

SET search_path TO order_command;

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM('PENDING_PAYMENT', 'PAID', 'CANCELED', 'CANCELLING');

CREATE TABLE order_command.orders (
    order_id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    restaurant_id UUID NOT NULL,
    payment_id UUID,
    driver_id UUID,
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

INSERT INTO order_command.orders (order_id, wallet_id, payment_id, customer_id, restaurant_id, ordered_at, address, order_status, total_price, error_message) VALUES
(
    'c96b37e9-d1ce-4293-9033-e3789a44b49b', '9418cf1b-83d7-4c6c-83da-b26216d35e0d', 'a126ca22-b049-4d19-851e-5596f1cfe400', 'b10e3b87-724a-47f6-946f-5b76286a61fd', 'a698f7a3-e107-49ce-8b83-ce0d6f8b08ec',
    '2023-04-10 10:39:37', 'test address', 'PAID', 23.99, ''
);

INSERT INTO order_command.order_items (order_item_id, product_id, quantity, price, order_id) VALUES
(
    'caca25f7-e01d-4471-9914-caaa146e9ac3', '02fa7a29-abfd-40f9-8923-1161e43a3a2f', 4, 23.99, 'c96b37e9-d1ce-4293-9033-e3789a44b49b'
);