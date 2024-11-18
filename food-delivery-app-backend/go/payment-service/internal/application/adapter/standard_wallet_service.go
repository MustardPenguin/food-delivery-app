package adapter

import (
	"database/sql"
	"errors"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository/wallet"
	"github.com/google/uuid"
)

type StandardWalletService struct {
	WalletRepository port.WalletRepository
}

func NewStandardWalletService(db *sql.DB) *StandardWalletService {
	return &StandardWalletService{
		WalletRepository: wallet.NewWalletSqlRepository(db),
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

	response, err := w.WalletRepository.SaveWallet(wallet)

	if err != nil {
		return entity.Wallet{}, err
	}

	return response, nil
}
