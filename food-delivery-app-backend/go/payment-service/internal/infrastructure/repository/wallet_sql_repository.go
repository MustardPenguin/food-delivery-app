package repository

import (
	"database/sql"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"math"
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

func (w *WalletSqlRepository) UpdateWallet(tx *sql.Tx, wallet entity.Wallet) (entity.Wallet, error) {

	query := `UPDATE payment.wallets SET balance = $1 RETURNING wallet_id`
	err := tx.QueryRow(query, wallet.Balance).Scan(&wallet.WalletId)
	if err != nil {
		return entity.Wallet{}, err
	}

	return wallet, nil
}

func (w *WalletSqlRepository) GetWalletsByCustomerId(id string) ([]entity.Wallet, error) {
	query := `SELECT wallet_id, customer_id, balance FROM payment.wallets WHERE customer_id = $1`

	rows, err := w.db.Query(query, id)
	var wallets []entity.Wallet
	if err != nil {
		return nil, err
	}
	defer rows.Close()
	for rows.Next() {
		var wallet entity.Wallet
		err = rows.Scan(&wallet.WalletId, &wallet.CustomerId, &wallet.Balance)
		if err != nil {
			return nil, err
		}
		wallets = append(wallets, wallet)
	}

	return wallets, nil
}

func (w *WalletSqlRepository) GetWalletById(walletId string) (entity.Wallet, error) {
	query := `SELECT wallet_id, customer_id, balance FROM payment.wallets WHERE wallet_id = $1`
	var wallet entity.Wallet
	err := w.db.QueryRow(query, walletId).Scan(&wallet.WalletId, &wallet.CustomerId, &wallet.Balance)
	if err != nil {
		return entity.Wallet{}, nil
	}
	wallet.Balance = math.Round(wallet.Balance*100) / 100
	return wallet, nil
}
