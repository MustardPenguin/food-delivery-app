package tests

import (
	"bytes"
	"encoding/json"
	"github.com/google/uuid"
	"io"
	"log"
	"net/http"
	"net/http/httptest"
	"payment-service/internal/api/controller"
	"payment-service/internal/application/adapter"
	"payment-service/internal/domain/entity"
	"testing"
)

func convertToJson(t *testing.T, body map[string]interface{}) []byte {
	t.Helper()
	j, err := json.Marshal(body)

	if err != nil {
		t.Errorf("error while parsing json: %v", err)
	}

	return j
}

func getBody[T any](t testing.TB, res *http.Response, data T) T {
	t.Helper()
	body, err := io.ReadAll(res.Body)
	if err != nil {
		t.Errorf("Unable to read response body: %v", http.StatusInternalServerError)
	}
	defer res.Body.Close()

	err = json.Unmarshal(body, &data)
	if err != nil {
		t.Errorf("Error parsing json: %v", err)
	}

	return data
}

type MockJwtHelper struct{}

var wcCustomerId string = uuid.NewString()

func (jwt *MockJwtHelper) GetSubjectFromToken(r *http.Request) (string, error) {
	return wcCustomerId, nil
}

func TestCreateWallet(t *testing.T) {
	command := map[string]interface{}{
		"InitialBalance": 50,
	}

	wc := controller.WalletController{
		WalletService: adapter.NewStandardWalletService(db),
		JwtHelper:     &MockJwtHelper{},
	}
	body := convertToJson(t, command)

	r, err := http.NewRequest("POST", "/api/v1/wallets", bytes.NewBuffer(body))
	if err != nil {
		t.Errorf("error creating request %v", err)
	}
	w := httptest.NewRecorder()
	wc.CreateWallet(w, r)

	res := w.Result()
	log.Print(res)
	got := getBody(t, res, entity.Wallet{})

	if got.CustomerId != wcCustomerId {
		t.Errorf("got %v want %v", got, wcCustomerId)
	}
	if got.Balance != 50 {
		t.Errorf("got %v want %v", got.Balance, command["InitialBalance"])
	}
}
