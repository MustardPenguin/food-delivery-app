package port

import "food-delivery-app-backend/payment-service/internal/domain/entity"

type WalletRepository interface {
	SaveWallet(wallet entity.Wallet) (entity.Wallet, error)
	GetWalletSByCustomerId(customerId string) ([]entity.Wallet, error)
}
