package wallet

import (
	"database/sql"
	"payment-service/internal/domain/entity"
)

type WalletSqlRepository struct {
	db *sql.DB
}

func NewWalletSqlRepository(db *sql.DB) *WalletSqlRepository {
	return &WalletSqlRepository{
		db: db,
	}
}

func (w *WalletSqlRepository) SaveWallet(wallet entity.Wallet) (entity.Wallet, error) {

	query := `INSERT INTO payment.wallets (wallet_id, customer_id, balance) VALUES ($1, $2, $3) RETURNING wallet_id`

	err := w.db.QueryRow(query, wallet.WalletId, wallet.CustomerId, wallet.Balance).Scan(&wallet.WalletId)

	if err != nil {
		return entity.Wallet{}, err
	}

	return wallet, nil
}

func (w *WalletSqlRepository) GetWalletByCustomerId(id string) (entity.Wallet, error) {

	var walletId, customerId string
	var balance float64

	query := `SELECT wallet_id, customer_id, balance FROM payment.wallets WHERE customer_id = $1`

	err := w.db.QueryRow(query, id).Scan(&walletId, &customerId, &balance)

	if err != nil {
		return entity.Wallet{}, nil
	}

	return entity.Wallet{
		WalletId: walletId, CustomerId: customerId, Balance: balance,
	}, nil
}
