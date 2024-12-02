package repository

import (
	"database/sql"
	"food-delivery-app-backend/libs/transaction"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type CreatePaymentSqlHandler struct {
	db                *sql.DB
	PaymentRepository port.PaymentRepository
	WalletRepository  port.WalletRepository
}

func NewCreatePaymentSqlHandler(db *sql.DB) *CreatePaymentSqlHandler {
	return &CreatePaymentSqlHandler{
		db:                db,
		PaymentRepository: NewPaymentSqlRepository(db),
		WalletRepository:  NewWalletSqlRepository(db),
	}
}

func (p *CreatePaymentSqlHandler) Handle(wallet entity.Wallet, payment entity.Payment) error {
	return transaction.RunInTx(p.db, func(tx *sql.Tx) error {

		_, err := p.PaymentRepository.SavePayment(tx, payment)
		if err != nil {
			return err
		}
		_, err = p.WalletRepository.UpdateWallet(tx, wallet)
		if err != nil {
			return err
		}

		return nil
	})
}
