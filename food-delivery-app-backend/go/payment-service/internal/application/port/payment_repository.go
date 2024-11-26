package port

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type PaymentRepository interface {
	SavePayment(tx *sql.Tx, payment entity.Payment) (entity.Payment, error)
}
