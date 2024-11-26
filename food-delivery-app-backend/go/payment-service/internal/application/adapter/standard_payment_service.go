package adapter

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
)

type StandardPaymentService struct {
	WalletRepository  port.WalletRepository
	PaymentRepository port.PaymentRepository
	DomainService     domain.PaymentDomainService
	db                *sql.DB
}

func NewStandardPaymentService(db *sql.DB) *StandardPaymentService {
	return &StandardPaymentService{
		WalletRepository:  repository.NewWalletSqlRepository(db),
		PaymentRepository: repository.NewPaymentSqlRepository(db),
		DomainService:     &domain.PaymentDomainServiceImpl{},
		db:                db,
	}
}

func (p *StandardPaymentService) PayOrder(payment entity.Payment) error {

	wallet, err := p.WalletRepository.GetWalletById(payment.WalletId)
	if err != nil {
		return err
	}
	err = p.DomainService.CreatePayment(&wallet, &payment)
	if err != nil {
		return err
	}

	tx, err := p.db.Begin()
	if err != nil {
		return err
	}
	defer tx.Rollback()

	err = tx.Commit()
	if err != nil {
		return err
	}

	return nil
}
