package port

import (
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type PaymentService interface {
	PayOrder(request dto.PaymentRequest) (entity.Payment, error)
}
