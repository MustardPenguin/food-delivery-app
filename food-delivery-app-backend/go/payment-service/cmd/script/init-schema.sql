
DROP SCHEMA IF EXISTS payment CASCADE;
CREATE SCHEMA payment;

CREATE TYPE payment.payment_status AS ENUM('COMPLETED', 'FAILED', 'REFUNDED', 'CANCELED');

CREATE TABLE payment.wallets (
    wallet_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    balance NUMERIC(10, 2) NOT NULL
);

CREATE TABLE payment.payments (
    payment_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    order_id UUID NOT NULL,
    wallet_id UUID NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    payment_status payment_status NOT NULL,
    error_message VARCHAR(1052) NOT NULL
);

CREATE TABLE payment.payment_created_events (
    event_id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);

INSERT INTO payment.wallets (wallet_id, customer_id, balance) VALUES
('f86b0456-0a1a-4c90-8f48-136541fb048d', 'a3001d04-22ab-4ce3-a220-4a8ea2503f8c', 1000
)