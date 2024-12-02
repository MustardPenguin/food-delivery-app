package port

import (
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type CreatePaymentHandler interface {
	Handle(wallet entity.Wallet, payment entity.Payment) error
}
