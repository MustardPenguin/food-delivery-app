package repository

import "time"

type PaymentEventEntity struct {
	EventId   string
	Payload   []byte
	CreatedAt time.Time
}
