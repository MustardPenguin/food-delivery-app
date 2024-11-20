
CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment.wallets (
    wallet_id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    balance NUMERIC(10, 2) NOT NULL
)