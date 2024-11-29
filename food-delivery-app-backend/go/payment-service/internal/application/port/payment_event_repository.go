package port

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/application/dto"
)

type PaymentEventRepository interface {
	Save(tx *sql.Tx, event dto.PaymentEventPayload) (dto.PaymentEventPayload, error)
}
