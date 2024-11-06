package domain

import (
	"errors"
	"fmt"
	"payment-service/internal/domain/entity"
)

type PaymentDomainServiceImpl struct{}

func NewPaymentDomainServiceImpl() PaymentDomainServiceImpl {
	return PaymentDomainServiceImpl{}
}

func (p *PaymentDomainServiceImpl) CreatePayment(wallet *entity.Wallet, payment *entity.Payment) error {

	if wallet.CustomerId != payment.CustomerId {
		return errors.New(fmt.Sprintf("customer of id %s does not own wallet of id %s", payment.CustomerId, wallet.CustomerId))
	}
	if wallet.Balance < payment.Amount {
		return errors.New(fmt.Sprintf("wallet of balance %f does not have enough credits for this payment of amount %f", wallet.Balance, payment.Amount))
	}

	payment.PaymentStatus = "completed"
	wallet.Balance -= payment.Amount

	return nil
}
