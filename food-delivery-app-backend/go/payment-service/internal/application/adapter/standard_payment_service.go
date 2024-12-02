package adapter

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
)

type StandardPaymentService struct {
	CreatePaymentHandler port.CreatePaymentHandler
	WalletRepository     port.WalletRepository
	DomainService        domain.PaymentDomainService
	db                   *sql.DB
}

func NewStandardPaymentService(db *sql.DB) *StandardPaymentService {
	return &StandardPaymentService{
		CreatePaymentHandler: repository.NewCreatePaymentSqlHandler(db),
		WalletRepository:     repository.NewWalletSqlRepository(db),
		DomainService:        domain.NewPaymentDomainServiceImpl(),
		db:                   db,
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

	err = p.CreatePaymentHandler.Handle(wallet, payment)
	if err != nil {
		return err
	}

	return nil
}
