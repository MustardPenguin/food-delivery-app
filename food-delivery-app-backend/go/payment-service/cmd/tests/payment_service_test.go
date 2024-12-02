package tests

import (
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
	"math"
	"testing"
)

type PaymentServiceTest struct {
	PaymentService    port.PaymentService
	PaymentRepository port.PaymentRepository
	WalletRepository  port.WalletRepository
	Payment           entity.Payment
}

func NewPaymentServiceTest() *PaymentServiceTest {
	return &PaymentServiceTest{
		PaymentService:    adapter.NewStandardPaymentService(db),
		PaymentRepository: repository.NewPaymentSqlRepository(db),
		WalletRepository:  repository.NewWalletSqlRepository(db),
		Payment: entity.Payment{
			CustomerId:    uuid.NewString(),
			WalletId:      uuid.NewString(),
			OrderId:       uuid.NewString(),
			PaymentId:     uuid.NewString(),
			CreatedAt:     time_util.GetCurrentTime(),
			PaymentStatus: "completed",
			Amount:        49.99},
	}
}

func TestPaymentService(t *testing.T) {
	pt := NewPaymentServiceTest()

	saved, err := pt.WalletRepository.SaveWallet(entity.Wallet{
		CustomerId: pt.Payment.CustomerId,
		WalletId:   pt.Payment.WalletId,
		Balance:    75,
	})

	err = pt.PaymentService.PayOrder(pt.Payment)
	if err != nil {
		t.Errorf("error paying for order: %v", err)
	}

	got, err := pt.PaymentRepository.GetPaymentById(pt.Payment.PaymentId)
	if got != pt.Payment {
		t.Errorf("got %v want %v", got, pt.Payment)
	}

	wallet, err := pt.WalletRepository.GetWalletById(pt.Payment.WalletId)
	if err != nil {
		t.Errorf("error getting wallet: %v", err)
	}

	wantBal := saved.Balance - pt.Payment.Amount
	wantBal = math.Round(wantBal*100) / 100
	if wallet.Balance != wantBal {
		t.Errorf("got %v balance want %v balance", wallet.Balance, wantBal)
	}
}
