package repository

import (
	"database/sql"
	"encoding/json"
	"food-delivery-app-backend/payment-service/internal/application/dto"
)

type PaymentEventSqlRepository struct {
}

func NewPaymentEventSqlRepository() *PaymentEventSqlRepository {
	return &PaymentEventSqlRepository{}
}

func (p *PaymentEventSqlRepository) Save(tx *sql.Tx, event dto.PaymentEventPayload) (dto.PaymentEventPayload, error) {
	payload, err := json.Marshal(event)
	if err != nil {
		return dto.PaymentEventPayload{}, err
	}

	query := `INSERT INTO payment.payment_created_events 
    (event_id, payload, created_at) 
	VALUES ($1, $2, $3) RETURNING event_id`
	err = tx.QueryRow(query, event.EventId, payload, event.CreatedAt).Scan(&event.EventId)
	if err != nil {
		return dto.PaymentEventPayload{}, err
	}

	return event, nil
}
