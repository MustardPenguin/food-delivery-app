
DROP SCHEMA IF EXISTS restaurant CASCADE;

CREATE SCHEMA restaurant;

CREATE TABLE restaurant.restaurants (
    restaurant_id UUID PRIMARY KEY,
    restaurant_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    owner_id UUID NOT NULL
);

CREATE TABLE restaurant.restaurant_products (
    product_id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    available BIT NOT NULL,
    price NUMERIC(10, 2) NOT NULL,

    restaurant_id UUID NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant.restaurants(restaurant_id)
);