package repository

import (
	"database/sql"
	"food-delivery-app-backend/libs/transaction"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"github.com/google/uuid"
)

type CreatePaymentSqlHandler struct {
	db                        *sql.DB
	PaymentEventSqlRepository port.PaymentEventRepository
	PaymentRepository         port.PaymentRepository
	WalletRepository          port.WalletRepository
}

func NewCreatePaymentSqlHandler(db *sql.DB) *CreatePaymentSqlHandler {
	return &CreatePaymentSqlHandler{
		db:                        db,
		PaymentEventSqlRepository: NewPaymentEventSqlRepository(),
		PaymentRepository:         NewPaymentSqlRepository(db),
		WalletRepository:          NewWalletSqlRepository(db),
	}
}

func (p *CreatePaymentSqlHandler) Handle(wallet entity.Wallet, payment entity.Payment) error {
	return transaction.RunInTx(p.db, func(tx *sql.Tx) error {

		_, err := p.PaymentRepository.SavePayment(tx, payment)
		if err != nil {
			return err
		}

		if wallet.WalletId != "" {
			_, err = p.WalletRepository.UpdateWallet(tx, wallet)
		}

		if err != nil {
			return err
		}
		_, err = p.PaymentEventSqlRepository.Save(tx, dto.PaymentEventPayload{
			EventId:       uuid.NewString(),
			PaymentId:     payment.PaymentId,
			OrderId:       payment.OrderId,
			CustomerId:    payment.CustomerId,
			WalletId:      payment.WalletId,
			Amount:        payment.Amount,
			PaymentStatus: payment.PaymentStatus,
			CreatedAt:     payment.CreatedAt,
			ErrorMessage:  payment.ErrorMessage,
		})

		return nil
	})
}
