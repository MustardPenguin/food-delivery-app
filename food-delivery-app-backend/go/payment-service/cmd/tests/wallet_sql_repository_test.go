package tests

import (
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"github.com/google/uuid"
	"testing"
)

var walletRepository port.WalletRepository
var wCustomerId string
var wWalletId string

func initWalletSqlRepositoryTest() {
	walletRepository = repository.NewWalletSqlRepository(db)
	wCustomerId = uuid.NewString()
	wWalletId = uuid.NewString()
}

func TestSaveWallet(t *testing.T) {
	initWalletSqlRepositoryTest()

	want := entity.Wallet{CustomerId: wCustomerId, WalletId: wWalletId, Balance: 50}
	got, err := walletRepository.SaveWallet(want)

	if err != nil {
		t.Errorf("error while saving wallet: %v", err)
	}
	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}

func TestGetWalletByCustomerId(t *testing.T) {

	want := entity.Wallet{CustomerId: wCustomerId, WalletId: wWalletId, Balance: 50}
	got, err := walletRepository.GetWalletSByCustomerId(wCustomerId)

	if err != nil {
		t.Errorf("error while getting wallet: %v", err)
	}
	if got[0] != want {
		t.Errorf("got %v want %v", got, want)
	}
}
