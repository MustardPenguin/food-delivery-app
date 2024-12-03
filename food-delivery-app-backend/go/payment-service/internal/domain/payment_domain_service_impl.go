package domain

import (
	"errors"
	"fmt"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"math"
)

type PaymentDomainServiceImpl struct{}

func NewPaymentDomainServiceImpl() *PaymentDomainServiceImpl {
	return &PaymentDomainServiceImpl{}
}

func (p *PaymentDomainServiceImpl) CreatePayment(wallet *entity.Wallet, payment *entity.Payment) error {

	if wallet.CustomerId != payment.CustomerId {
		payment.PaymentStatus = "failed"
		payment.ErrorMessage = "customer does not own wallet id"
		return errors.New(fmt.Sprintf("customer of id %s does not own wallet of id %s", payment.CustomerId, wallet.CustomerId))
	}
	if wallet.Balance < payment.Amount {
		payment.PaymentStatus = "failed"
		payment.ErrorMessage = "wallet balance not enough"
		return errors.New(fmt.Sprintf("wallet of balance %f does not have enough credits for this payment of amount %f", wallet.Balance, payment.Amount))
	}

	payment.PaymentStatus = "completed"

	result := wallet.Balance - payment.Amount
	bal := math.Round(result*100) / 100
	wallet.Balance = bal

	return nil
}
