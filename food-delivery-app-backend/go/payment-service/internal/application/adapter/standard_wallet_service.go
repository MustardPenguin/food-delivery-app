package adapter

import (
	"database/sql"
	"errors"
	"fmt"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
)

type StandardWalletService struct {
	WalletRepository port.WalletRepository
	db               *sql.DB
}

func NewStandardWalletService(db *sql.DB) *StandardWalletService {
	return &StandardWalletService{
		WalletRepository: repository.NewWalletSqlRepository(db),
		db:               db,
	}
}

func (w *StandardWalletService) CreateWallet(command dto.CreateWalletCommand, customerId string) (entity.Wallet, error) {

	if command.InitialBalance < 0 {
		return entity.Wallet{}, errors.New("initial balance cannot be negative")
	}

	wallet := entity.Wallet{
		CustomerId: customerId,
		WalletId:   uuid.NewString(),
		Balance:    command.InitialBalance,
	}

	fmt.Printf("\n s: %v", w)
	fmt.Printf("\n db: %v", w.db)
	tx, err := w.db.Begin()
	if err != nil {
		return entity.Wallet{}, nil
	}
	defer tx.Rollback()

	response, err := w.WalletRepository.SaveWallet(tx, wallet)
	if err != nil {
		return entity.Wallet{}, err
	}
	err = tx.Commit()
	if err != nil {
		return entity.Wallet{}, err
	}

	return response, nil
}
