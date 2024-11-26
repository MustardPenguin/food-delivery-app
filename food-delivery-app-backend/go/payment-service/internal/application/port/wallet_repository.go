package port

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
)

type WalletRepository interface {
	SaveWallet(tx *sql.Tx, wallet entity.Wallet) (entity.Wallet, error)
	UpdateWallet(tx *sql.Tx, wallet entity.Wallet) (entity.Wallet, error)
	GetWalletsByCustomerId(customerId string) ([]entity.Wallet, error)
	GetWalletById(walletId string) (entity.Wallet, error)
}
