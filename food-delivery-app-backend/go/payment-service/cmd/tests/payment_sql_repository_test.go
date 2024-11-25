package tests

import (
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
	"testing"
	"time"
)

var paymentRepository port.PaymentRepository
var payment entity.Payment

func initPaymentSqlRepositoryTest() {
	paymentRepository = repository.NewPaymentSqlRepository(db)
	payment = entity.Payment{
		CustomerId:    uuid.NewString(),
		WalletId:      uuid.NewString(),
		OrderId:       uuid.NewString(),
		PaymentId:     uuid.NewString(),
		CreatedAt:     time.Now().UTC(),
		PaymentStatus: "completed",
		Amount:        49.99,
	}
}

func TestSavePayment(t *testing.T) {
	initPaymentSqlRepositoryTest()

	got, err := paymentRepository.SavePayment(payment)
	if err != nil {
		t.Errorf("error saving payment: %v", err)
	}
	if got != payment {
		t.Errorf("got %v want %v", got, payment)
	}
}
