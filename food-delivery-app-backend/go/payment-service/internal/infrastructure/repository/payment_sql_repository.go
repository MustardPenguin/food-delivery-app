package repository

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"strings"
)

type PaymentSqlRepository struct {
	db *sql.DB
}

func NewPaymentSqlRepository(db *sql.DB) *PaymentSqlRepository {
	return &PaymentSqlRepository{
		db: db,
	}
}

func (p *PaymentSqlRepository) SavePayment(payment entity.Payment) (entity.Payment, error) {

	query := `INSERT INTO payment.payments 
    	(payment_id, customer_id, order_id, wallet_id, amount, created_at, payment_status) 
		VALUES ($1, $2, $3, $4, $5, $6, $7) RETURNING payment_id`

	err := p.db.QueryRow(query, payment.PaymentId, payment.CustomerId, payment.OrderId, payment.WalletId,
		payment.Amount, payment.CreatedAt, strings.ToUpper(string(payment.PaymentStatus))).Scan(&payment.PaymentId)

	if err != nil {
		return entity.Payment{}, err
	}
	return payment, nil
}
