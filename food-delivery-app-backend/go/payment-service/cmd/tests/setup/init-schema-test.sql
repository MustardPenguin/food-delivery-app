
CREATE SCHEMA payment;

CREATE TYPE payment_status AS ENUM('COMPLETED', 'FAILED', 'REFUNDED', 'CANCELED');

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
    payment_status payment_status NOT NULL
);

CREATE TABLE payment.payment_created_events (
    event_id UUID PRIMARY KEY,
    payload JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL
);