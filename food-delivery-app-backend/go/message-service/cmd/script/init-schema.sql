
DROP SCHEMA IF EXISTS message CASCADE;
CREATE SCHEMA message;

CREATE TABLE message.messages (
    message_id UUID PRIMARY KEY,
    author_id UUID NOT NULL,
    order_id UUID NOT NULL,
    content VARCHAR(1024) NOT NULL,
    date TIMESTAMP NOT NULL
);