package controller

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestCreateWallet(t *testing.T) {
	wc := NewWalletController()

	r, err := http.NewRequest("POST", "/api/v1/wallets", nil)
	if err != nil {
		t.Errorf("error creating request %v", err)
	}
	w := httptest.NewRecorder()
	wc.CreateWallet(w, r)

}
