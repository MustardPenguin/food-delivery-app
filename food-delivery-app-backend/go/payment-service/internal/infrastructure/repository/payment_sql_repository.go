package repository

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/domain/valueobject"
	"strings"
	"time"
)

type PaymentSqlRepository struct {
	db *sql.DB
}

func NewPaymentSqlRepository(db *sql.DB) *PaymentSqlRepository {
	return &PaymentSqlRepository{
		db: db,
	}
}

func (p *PaymentSqlRepository) SavePayment(tx *sql.Tx, payment entity.Payment) (entity.Payment, error) {

	query := `INSERT INTO payment.payments 
    	(payment_id, customer_id, order_id, wallet_id, amount, created_at, payment_status, error_message) 
		VALUES ($1, $2, $3, $4, $5, $6, $7, $8) RETURNING payment_id`

	err := tx.QueryRow(query, payment.PaymentId, payment.CustomerId, payment.OrderId, payment.WalletId,
		payment.Amount, payment.CreatedAt, strings.ToUpper(string(payment.PaymentStatus)), payment.ErrorMessage).Scan(&payment.PaymentId)

	if err != nil {
		return entity.Payment{}, err
	}
	return payment, nil
}

func (p *PaymentSqlRepository) GetPaymentById(paymentId string) (entity.Payment, error) {
	var payment entity.Payment

	query := `SELECT payment_id, customer_id, order_id, wallet_id, amount, created_at, payment_status
              FROM payment.payments WHERE payment_id = $1`

	err := p.db.QueryRow(query, paymentId).Scan(&payment.PaymentId, &payment.CustomerId, &payment.OrderId,
		&payment.WalletId, &payment.Amount, &payment.CreatedAt, &payment.PaymentStatus)
	if err != nil {
		return entity.Payment{}, err
	}
	payment.PaymentStatus = valueobject.PaymentStatus(strings.ToLower(string(payment.PaymentStatus)))
	payment.CreatedAt = payment.CreatedAt.Truncate(time.Second).UTC()

	return payment, nil
}
