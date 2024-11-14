package port

import (
	"payment-service/internal/application/dto"
	"payment-service/internal/domain/entity"
)

type WalletService interface {
	CreateWallet(command dto.CreateWalletCommand, customerId string) (entity.Wallet, error)
}
