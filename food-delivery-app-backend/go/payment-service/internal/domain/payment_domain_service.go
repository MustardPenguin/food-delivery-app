package domain

import "food-delivery-app-backend/payment-service/internal/domain/entity"

type PaymentDomainService interface {
	CreatePayment(wallet *entity.Wallet, payment *entity.Payment) error
}
