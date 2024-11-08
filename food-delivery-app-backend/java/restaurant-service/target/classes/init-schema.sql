
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

INSERT INTO restaurant.restaurants (restaurant_id, name, address, owner_id, active) VALUES
(
    'f86b0456-0a1a-4c90-8f48-136541fb048e', 'restaurant name', 'restaurant address', '6b6379b1-b589-4a81-b0d1-2eb1dc3d55f7', false
);

INSERT INTO restaurant.products (product_id, name, available, price, description, restaurant_id) VALUES
(
    '84b17918-4aa6-4be7-8fde-f61b7e022438', 'name', true, 9.99, 'description', 'f86b0456-0a1a-4c90-8f48-136541fb048e'
);

INSERT INTO restaurant.products (product_id, name, available, price, description, restaurant_id) VALUES
(
    '84b17918-4aa6-4be7-8fde-f61b7e022456', 'name', true, 12.5, 'description', 'f86b0456-0a1a-4c90-8f48-136541fb048e'
);

INSERT INTO restaurant.products (product_id, name, available, price, description, restaurant_id) VALUES
(
    '84b17918-4aa6-4be7-8fde-f61b7e022221', 'name', false, 12.5, 'description', 'f86b0456-0a1a-4c90-8f48-136541fb048e'
);