package tests

import (
	"bytes"
	"encoding/json"
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
	"testing"
)

type PaymentEventSqlRepositoryTest struct {
	PaymentEventRepository port.PaymentEventRepository
}

func NewPaymentEventSqlRepositoryTest() *PaymentEventSqlRepositoryTest {
	return &PaymentEventSqlRepositoryTest{
		PaymentEventRepository: repository.NewPaymentEventSqlRepository(),
	}
}

func TestSaveEvent(t *testing.T) {
	pes := NewPaymentEventSqlRepositoryTest()
	payload := dto.PaymentEventPayload{
		EventId:       uuid.NewString(),
		PaymentId:     uuid.NewString(),
		OrderId:       uuid.NewString(),
		CustomerId:    uuid.NewString(),
		WalletId:      uuid.NewString(),
		Amount:        50,
		PaymentStatus: "completed",
		CreatedAt:     time_util.GetCurrentTime(),
		ErrorMessage:  "",
	}

	tx, err := db.Begin()
	if err != nil {
		t.Errorf("error starting transaction: %v", err)
	}
	defer tx.Rollback()
	_, err = pes.PaymentEventRepository.Save(tx, payload)
	if err != nil {
		t.Errorf("error saving event: %v", err)
	}
	err = tx.Commit()
	if err != nil {
		t.Errorf("error committing transaction: %v", err)
	}

	var got repository.PaymentEventEntity
	query := `SELECT event_id, payload, created_at FROM payment.payment_created_events WHERE event_id = $1`
	err = db.QueryRow(query, payload.EventId).Scan(&got.EventId, &got.Payload, &got.CreatedAt)
	if err != nil {
		t.Errorf("error getting event: %v", err)
	}
	j, err := json.Marshal(payload)
	if err != nil {
		t.Errorf("error marshalling json: %v", err)
	}
	if bytes.Equal(got.Payload, j) {
		t.Errorf("got %v want %v", got.Payload, j)
	}
}
