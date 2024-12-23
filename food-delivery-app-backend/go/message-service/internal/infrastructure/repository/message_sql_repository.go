package repository

import (
	"database/sql"
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/message-service/internal/domain/entity"
)

type MessageSqlRepository struct {
	db *sql.DB
}

func NewMessageSqlRepository(db *sql.DB) *MessageSqlRepository {
	return &MessageSqlRepository{
		db: db,
	}
}

func (m *MessageSqlRepository) Save(msg entity.Message) (entity.Message, error) {

	query := `INSERT INTO message.messages (message_id, author_id, order_id, content, sent_at) VALUES ($1,  $2, $3, $4, $5) RETURNING message_id`
	err := m.db.QueryRow(query, msg.MessageId, msg.AuthorId, msg.OrderId, msg.Content, msg.SentAt).Scan(&msg.MessageId)
	if err != nil {
		return entity.Message{}, err
	}

	return msg, nil
}

func (m *MessageSqlRepository) GetById(id string) (entity.Message, error) {
	msg := entity.Message{}
	query := `SELECT message_id, author_id, order_id, content, sent_at FROM message.messages WHERE message_id = $1`
	err := m.db.QueryRow(query, id).Scan(&msg.MessageId, &msg.AuthorId, &msg.OrderId, &msg.Content, &msg.SentAt)
	if err != nil {
		return msg, nil
	}
	msg.SentAt = time_util.Truncate(msg.SentAt)
	return msg, nil
}

func (m *MessageSqlRepository) GetByOrderId(id string) ([]entity.Message, error) {
	query := `SELECT message_id, author_id, order_id, content, sent_at FROM message.messages WHERE order_id = $1 ORDER BY sent_at`
	rows, err := m.db.Query(query, id)
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	var msgs []entity.Message
	for rows.Next() {
		var msg entity.Message
		err = rows.Scan(&msg.MessageId, &msg.AuthorId, &msg.OrderId, &msg.Content, &msg.SentAt)
		if err != nil {
			return msgs, err
		}
		msgs = append(msgs, msg)
	}
	return msgs, nil
}
