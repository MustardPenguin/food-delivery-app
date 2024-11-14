package tests

import (
	"github.com/google/uuid"
	"payment-service/internal/domain/entity"
	"payment-service/internal/infrastructure/repository/wallet"
	"testing"
)

var walletRepository *wallet.WalletSqlRepository
var customerId string
var walletId string

func initSqlRepositoryTest() {
	walletRepository = wallet.NewWalletSqlRepository(db)
	customerId = uuid.NewString()
	walletId = uuid.NewString()
}

func TestSaveWallet(t *testing.T) {
	initSqlRepositoryTest()

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
