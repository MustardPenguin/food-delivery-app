package tests

import (
	"bytes"
	"food-delivery-app-backend/libs/controller_util"
	"food-delivery-app-backend/payment-service/internal/api/controller"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"github.com/google/uuid"
	"net/http"
	"net/http/httptest"
	"testing"
)

func convertToJson(t *testing.T, body map[string]interface{}) []byte {
	t.Helper()
	j, err := controller_util.ConvertToJson(body)

	if err != nil {
		t.Errorf("error while parsing json: %v", err)
	}

	return j
}

func getBody[T any](t testing.TB, r *http.Response, data T) T {
	t.Helper()

	d, err := controller_util.GetBody(r, data)

	if err != nil {
		t.Errorf("error getting response body: %v", err)
	}

	return d
}

type MockJwtHelper struct{}

type WalletControllerTest struct {
	CustomerId string
}

func NewWalletControllerTest() *WalletControllerTest {
	return &WalletControllerTest{
		CustomerId: uuid.NewString(),
	}
}

var wcCustomerId string = uuid.NewString()

func (jwt *MockJwtHelper) GetSubjectFromToken(r *http.Request) (string, error) {
	return wcCustomerId, nil
}

func TestWalletController(t *testing.T) {
	var wallet entity.Wallet
	wc := controller.WalletController{
		WalletService: adapter.NewStandardWalletService(db),
		JwtHelper:     &MockJwtHelper{},
	}
	t.Run("create wallet", func(t *testing.T) {
		command := map[string]interface{}{
			"InitialBalance": 50,
		}

		body := convertToJson(t, command)

		r, err := http.NewRequest("POST", "/api/v1/wallets", bytes.NewBuffer(body))
		if err != nil {
			t.Errorf("error creating request %v", err)
		}
		w := httptest.NewRecorder()
		wc.CreateWallet(w, r)

		res := w.Result()
		got := getBody(t, res, entity.Wallet{})

		if got.CustomerId != wcCustomerId {
			t.Errorf("got %v want %v", got, wcCustomerId)
		}
		if got.Balance != 50 {
			t.Errorf("got %v want %v", got.Balance, command["InitialBalance"])
		}
		wallet = got
	})
	t.Logf("\n want wallet of %v", wallet)
	t.Run("get wallet", func(t *testing.T) {

		w := httptest.NewRecorder()
		wc.GetWalletByIdHandle(w, wallet.WalletId)
		res := w.Result()
		t.Logf("res: %v", res)
		got := getBody(t, res, entity.Wallet{})

		if got != wallet {
			t.Errorf("got %v want %v", got, wallet)
		}
	})
}
