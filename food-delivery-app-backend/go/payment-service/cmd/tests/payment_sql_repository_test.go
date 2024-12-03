package tests

import (
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"testing"
)

type PaymentSqlRepositoryTest struct {
	PaymentRepository port.PaymentRepository
	Payment           entity.Payment
}

func NewPaymentSqlRepositoryTest() *PaymentSqlRepositoryTest {
	payment := entity.Payment{
		CustomerId:    "57bb7ee8-7bcc-407b-86a9-1a6b910b7ffa",
		WalletId:      "090995e5-7d00-4605-95a7-fef83c9c3d3b",
		OrderId:       "090995e5-7d00-4605-95a7-fef83c9c3d3b",
		PaymentId:     "c7a0999b-3adf-46f0-a406-747715b0c9ee",
		CreatedAt:     time_util.GetCurrentTime(),
		PaymentStatus: "completed",
		Amount:        49.99,
		ErrorMessage:  "",
	}
	return &PaymentSqlRepositoryTest{
		PaymentRepository: repository.NewPaymentSqlRepository(db),
		Payment:           payment,
	}
}

func TestSavePayment(t *testing.T) {
	pst := NewPaymentSqlRepositoryTest()

	tx, err := db.Begin()
	if err != nil {
		t.Errorf("error starting transaction: %v", err)
	}
	defer tx.Rollback()
	got, err := pst.PaymentRepository.SavePayment(tx, pst.Payment)

	if err != nil {
		t.Errorf("error saving sqlPayment: %v", err)
	}
	err = tx.Commit()
	if err != nil {
		t.Errorf("error commiting transaction: %v", err)
	}
	if got != pst.Payment {
		t.Errorf("got %v \n want %v", got, pst.Payment)
	}
}

func TestGetPaymentById(t *testing.T) {
	pst := NewPaymentSqlRepositoryTest()
	got, err := pst.PaymentRepository.GetPaymentById(pst.Payment.PaymentId)
	if err != nil {
		t.Errorf("error getting sqlPayment: %v", err)
	}
	if got != pst.Payment {
		t.Errorf("got %v want %v", got, pst.Payment)
	}
}
