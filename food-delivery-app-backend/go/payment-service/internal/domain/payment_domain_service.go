package domain

import "payment-service/internal/domain/entity"

type PaymentDomainService interface {
	CreatePayment(wallet entity.Wallet, payment entity.Payment) (*entity.Payment, error)
}
