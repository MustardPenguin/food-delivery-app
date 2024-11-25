package port

import "food-delivery-app-backend/payment-service/internal/domain/entity"

type PaymentService interface {
	PayOrder(payment entity.Payment) error
}
