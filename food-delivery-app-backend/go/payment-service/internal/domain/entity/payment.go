package entity

import (
	"food-delivery-app-backend/payment-service/internal/domain/valueobject"
	"time"
)

type Payment struct {
	PaymentId     string
	OrderId       string
	CustomerId    string
	WalletId      string
	Amount        float64
	PaymentStatus valueobject.PaymentStatus
	CreatedAt     time.Time
}
