package dto

import (
	"food-delivery-app-backend/payment-service/internal/domain/valueobject"
	"time"
)

type PaymentEventPayload struct {
	EventId       string
	PaymentId     string
	OrderId       string
	CustomerId    string
	WalletId      string
	Amount        float64
	PaymentStatus valueobject.PaymentStatus
	CreatedAt     time.Time
}
