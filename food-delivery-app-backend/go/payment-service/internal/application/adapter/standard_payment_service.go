package adapter

import (
	"database/sql"
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
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

func (p *StandardPaymentService) PayOrder(req dto.PaymentRequest) (entity.Payment, error) {
	payment := entity.Payment{
		PaymentId:     uuid.NewString(),
		OrderId:       req.OrderId,
		CustomerId:    req.CustomerId,
		WalletId:      req.WalletId,
		Amount:        req.Amount,
		PaymentStatus: "",
		CreatedAt:     time_util.GetCurrentTime(),
		ErrorMessage:  "",
	}
	payment, err := p.handlePayment(payment)
	if err == nil {
		return payment, err
	}
	err = p.CreatePaymentHandler.Handle(entity.Wallet{WalletId: ""}, payment)
	if err != nil {
		return entity.Payment{}, err
	}
	return payment, nil
}

func (p *StandardPaymentService) handlePayment(payment entity.Payment) (entity.Payment, error) {
	wallet, err := p.WalletRepository.GetWalletById(payment.WalletId)
	if err != nil {
		return payment, err
	}
	err = p.DomainService.CreatePayment(&wallet, &payment)
	if err != nil {
		return payment, err
	}

	err = p.CreatePaymentHandler.Handle(wallet, payment)
	if err != nil {
		return payment, err
	}

	return payment, nil
}
