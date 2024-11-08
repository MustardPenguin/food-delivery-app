package tests

import (
	"context"
	"database/sql"
	"github.com/google/uuid"
	"log"
	"payment-service/cmd/tests/setup"
	"payment-service/internal/domain/entity"
	"payment-service/internal/infrastructure/repository/wallet"
	"testing"
)

var walletRepository *wallet.WalletSqlRepository
var customerId string
var walletId string

func TestMain(m *testing.M) {

	ctx := context.Background()
	pgContainer, db := setup.Startup(ctx)

	defer func() {
		if err := pgContainer.Terminate(ctx); err != nil {
			log.Fatalf("failed to terminate container: %s", err)
		}
	}()

	defer func(db *sql.DB) {
		err := db.Close()
		if err != nil {
			log.Fatalf("Error closing db")
		}
	}(db)

	walletRepository = wallet.NewWalletSqlRepository(db)
	customerId = uuid.NewString()
	walletId = uuid.NewString()

	m.Run()
}

func TestSaveWallet(t *testing.T) {

	want := entity.Wallet{CustomerId: customerId, WalletId: walletId, Balance: 50}
	got, err := walletRepository.SaveWallet(want)

	if err != nil {
		t.Errorf("error while saving wallet: %v", err)
	}
	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}

func TestGetWalletByCustomerId(t *testing.T) {

	want := entity.Wallet{CustomerId: customerId, WalletId: walletId, Balance: 50}
	got, err := walletRepository.GetWalletByCustomerId(customerId)

	if err != nil {
		t.Errorf("error while getting wallet: %v", err)
	}
	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}
