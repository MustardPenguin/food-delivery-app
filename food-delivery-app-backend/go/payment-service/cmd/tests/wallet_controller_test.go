package tests

import (
	"bytes"
	common "food-delivery-app-backend/common/api"
	"food-delivery-app-backend/payment-service/internal/api/controller"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"github.com/google/uuid"
	"log"
	"net/http"
	"net/http/httptest"
	"testing"
)

func convertToJson(t *testing.T, body map[string]interface{}) []byte {
	t.Helper()
	j, err := common.ConvertToJson(body)

	if err != nil {
		t.Errorf("error while parsing json: %v", err)
	}

	return j
}

func getBody[T any](t testing.TB, r *http.Response, data T) T {
	t.Helper()

	d, err := common.GetBody(r, data)

	if err != nil {
		t.Errorf("error getting response body: %v", err)
	}

	return d
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
