package port

import (
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type WalletService interface {
	CreateWallet(command dto.CreateWalletCommand, customerId string) (entity.Wallet, error)
	GetWalletById(walletId string) (entity.Wallet, error)
}
