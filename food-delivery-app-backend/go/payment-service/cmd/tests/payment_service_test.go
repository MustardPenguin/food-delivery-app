package tests

import (
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/domain/valueobject"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
	"testing"
)

type PaymentServiceTest struct {
	PaymentService    port.PaymentService
	PaymentRepository port.PaymentRepository
	WalletRepository  port.WalletRepository
	PaymentRequest    dto.PaymentRequest
}

func NewPaymentServiceTest() *PaymentServiceTest {
	return &PaymentServiceTest{
		PaymentService:    adapter.NewStandardPaymentService(db),
		PaymentRepository: repository.NewPaymentSqlRepository(db),
		WalletRepository:  repository.NewWalletSqlRepository(db),
		PaymentRequest: dto.PaymentRequest{
			CustomerId: "f12521bd-f580-408b-9e11-5d0d4a4fe785",
			WalletId:   "552343b7-2e0b-42df-bd24-a947bfc3a401",
			OrderId:    uuid.NewString(),
			Amount:     50},
	}
}

func TestSuccessfulPayment(t *testing.T) {
	pt := NewPaymentServiceTest()

	savedWallet, err := pt.WalletRepository.SaveWallet(entity.Wallet{
		CustomerId: pt.PaymentRequest.CustomerId,
		WalletId:   pt.PaymentRequest.WalletId,
		Balance:    75,
	})

	payment, err := pt.PaymentService.PayOrder(pt.PaymentRequest)
	if err != nil {
		t.Errorf("error paying for order: %v", err)
	}

	got, err := pt.PaymentRepository.GetPaymentById(payment.PaymentId)
	if got != payment {
		t.Errorf("got %v want %v", got, payment)
	}

	wallet, err := pt.WalletRepository.GetWalletById(pt.PaymentRequest.WalletId)
	if err != nil {
		t.Errorf("error getting wallet: %v", err)
	}

	wantBal := savedWallet.Balance - pt.PaymentRequest.Amount
	if wantBal != wallet.Balance {
		t.Errorf("got %v want %v", wallet.Balance, wantBal)
	}
}

func TestFailedPayment(t *testing.T) {
	pt := NewPaymentServiceTest()

	pt.PaymentRequest.Amount = 9999
	payment, err := pt.PaymentService.PayOrder(pt.PaymentRequest)
	if err != nil {
		t.Errorf("error paying for order: %v", err)
	}

	got, err := pt.PaymentRepository.GetPaymentById(payment.PaymentId)
	if got.PaymentStatus != valueobject.FAILED {
		t.Errorf("expected failed payment got %v", got.PaymentStatus)
	}
}
