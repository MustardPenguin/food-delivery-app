package adapter

import "food-delivery-app-backend/payment-service/internal/domain/entity"

type StandardPaymentService struct {
}

func NewStandardPaymentService() *StandardPaymentService {
	return &StandardPaymentService{}
}

func (s *StandardPaymentService) PayOrder(payment entity.Payment) error {

	return nil
}
