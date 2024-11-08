
CREATE SCHEMA payment;

CREATE TABLE payment.wallets (
    wallet_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    balance NUMERIC(10, 2) NOT NULL
)