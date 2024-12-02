package tests

import (
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"food-delivery-app-backend/payment-service/internal/infrastructure/repository"
	"testing"
)

type WalletSqlRepositoryTest struct {
	WalletRepository port.WalletRepository
	CustomerId       string
	WalletId         string
}

func NewWalletSqlRepositoryTest() *WalletSqlRepositoryTest {
	return &WalletSqlRepositoryTest{
		WalletRepository: repository.NewWalletSqlRepository(db),
		CustomerId:       "a593ff1a-08cb-485b-8277-fa5d30f8320f",
		WalletId:         "49f20d33-fc13-43d1-a9c9-3ce1cf6da761",
	}
}

func TestSaveWallet(t *testing.T) {
	ws := NewWalletSqlRepositoryTest()

	want := entity.Wallet{CustomerId: ws.CustomerId, WalletId: ws.WalletId, Balance: 50}
	got, err := ws.WalletRepository.SaveWallet(want)

	if err != nil {
		t.Errorf("error while saving wallet: %v", err)
	}

	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}

func TestGetWalletByCustomerId(t *testing.T) {
	ws := NewWalletSqlRepositoryTest()

	want := entity.Wallet{CustomerId: ws.CustomerId, WalletId: ws.WalletId, Balance: 50}
	got, err := ws.WalletRepository.GetWalletsByCustomerId(ws.CustomerId)

	if err != nil {
		t.Errorf("error while getting wallet: %v", err)
	}
	if got[0] != want {
		t.Errorf("got %v want %v", got, want)
	}
}

func TestGetWalletByWalletId(t *testing.T) {
	ws := NewWalletSqlRepositoryTest()
	want := entity.Wallet{CustomerId: ws.CustomerId, WalletId: ws.WalletId, Balance: 50}
	got, err := ws.WalletRepository.GetWalletById(ws.WalletId)
	if err != nil {
		t.Errorf("error getting wallet by id: %v", err)
	}
	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}

func TestUpdateWallet(t *testing.T) {
	ws := NewWalletSqlRepositoryTest()
	want := entity.Wallet{CustomerId: ws.CustomerId, WalletId: ws.WalletId, Balance: 25}
	tx, err := db.Begin()
	if err != nil {
		t.Errorf("error starting transaction: %v", err)
	}
	defer tx.Rollback()
	_, err = ws.WalletRepository.UpdateWallet(tx, want)
	if err != nil {
		t.Errorf("error updating wallet: %v", err)
	}
	err = tx.Commit()
	if err != nil {
		t.Errorf("error committing transaction: %v", err)
	}
	got, err := ws.WalletRepository.GetWalletById(ws.WalletId)
	if err != nil {
		t.Errorf("error getting wallet: %v", err)
	}
	if got != want {
		t.Errorf("got %v want %v", got, want)
	}
}
