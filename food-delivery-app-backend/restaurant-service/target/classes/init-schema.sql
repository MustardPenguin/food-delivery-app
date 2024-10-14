
DROP SCHEMA IF EXISTS restaurant CASCADE;

CREATE SCHEMA restaurant;

CREATE TABLE restaurant.restaurants (
    restaurant_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    owner_id UUID NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE restaurant.products (
    product_id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    description VARCHAR(1024) NOT NULL,

    restaurant_id UUID NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant.restaurants(restaurant_id)
);