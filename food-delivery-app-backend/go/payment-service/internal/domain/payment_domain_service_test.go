package domain

import (
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"testing"
	"time"
)

func TestCreateValidPayment(t *testing.T) {
	service := PaymentDomainServiceImpl{}
	customerId := "customerId"
	walletId := "wallet_id"
	amount := 29.99
	balance := 50.00
	payment := entity.Payment{
		CustomerId: customerId,
		WalletId:   walletId,
		Amount:     amount,
		CreatedAt:  time.Time{},
	}
	want := entity.Payment{
		PaymentId:     "",
		OrderId:       "",
		CustomerId:    customerId,
		WalletId:      walletId,
		Amount:        amount,
		PaymentStatus: "completed",
		CreatedAt:     time.Time{},
	}
	wallet := entity.Wallet{
		CustomerId: customerId,
		WalletId:   walletId,
		Balance:    balance,
	}

	err := service.CreatePayment(&wallet, &payment)

	if err != nil {
		t.Errorf("err while creating payment: %v", err)
	}
	if payment != want {
		t.Errorf("got %v want %v", payment, want)
	}
	wantBalance := balance - amount
	if wallet.Balance != wantBalance {
		t.Errorf("want %f got %f", wantBalance, wallet.Balance)
	}
}

func TestCreateInvalidPayment(t *testing.T) {
	service := PaymentDomainServiceImpl{}
	t.Run("not own wallet", func(t *testing.T) {
		wallet := entity.Wallet{CustomerId: "1"}
		payment := entity.Payment{CustomerId: "2"}
		err := service.CreatePayment(&wallet, &payment)
		if err == nil {
			t.Errorf("expected err got nil")
		}
	})
	t.Run("insufficient balance", func(t *testing.T) {
		wallet := entity.Wallet{CustomerId: "1", Balance: 5}
		payment := entity.Payment{CustomerId: "1", Amount: 10}
		err := service.CreatePayment(&wallet, &payment)
		if err == nil {
			t.Errorf("expected err got nil")
		}
	})
}
