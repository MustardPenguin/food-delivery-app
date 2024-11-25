package port

import "food-delivery-app-backend/payment-service/internal/domain/entity"

type PaymentRepository interface {
	SavePayment(entity.Payment) (entity.Payment, error)
}
