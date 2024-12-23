
DROP SCHEMA IF EXISTS message CASCADE;
CREATE SCHEMA message;

CREATE TABLE message.messages (
    message_id UUID PRIMARY KEY,
    author_id UUID NOT NULL,
    order_id UUID NOT NULL,
    content VARCHAR(1024) NOT NULL,
    sent_at TIMESTAMP NOT NULL
);

INSERT INTO message.messages VALUES
('6f92f3ef-8bf2-4ebc-911d-38e3b94dc0f5', 'ce07989b-c110-42e5-a48b-767e4966690e', '6ee03c6e-bda5-468f-94e3-ef50fe36046e', 'content 1', '2024-12-25 10:39:37');

INSERT INTO message.messages VALUES
('3e642f4b-8e9b-4045-93f1-86556ce1aeed', 'ce07989b-c110-42e5-a48b-767e4966690e', '6ee03c6e-bda5-468f-94e3-ef50fe36046e', 'content 2', '2024-12-25 10:40:37');

INSERT INTO message.messages VALUES
('e31c3d53-4f71-47ab-bab1-5398ea67ee3d', '7eb00463-7235-404e-a0aa-8388362ec326', '6ee03c6e-bda5-468f-94e3-ef50fe36046e', 'content 3', '2024-12-25 10:41:37');

INSERT INTO message.messages VALUES
('8ead38ef-e973-4747-a9e9-df5efbc51564', '7eb00463-7235-404e-a0aa-8388362ec326', '6ee03c6e-bda5-468f-94e3-ef50fe36046e', 'content 4', '2024-12-25 10:42:37');

