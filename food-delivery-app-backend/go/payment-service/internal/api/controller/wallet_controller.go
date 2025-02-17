package controller

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"food-delivery-app-backend/libs/jwt_util"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"io"
	"net/http"
)

type WalletController struct {
	WalletService port.WalletService
	JwtHelper     jwt_util.JwtHelper
}

func NewWalletController(db *sql.DB) *WalletController {
	return &WalletController{
		WalletService: adapter.NewStandardWalletService(db),
		JwtHelper:     jwt_util.NewJwtHelperImpl(),
	}
}

func (wc *WalletController) CreateWallet(w http.ResponseWriter, r *http.Request) {

	body, err := io.ReadAll(r.Body)
	if err != nil {
		msg := fmt.Sprintf("error reading request body: %v", err)
		http.Error(w, msg, http.StatusInternalServerError)
		return
	}
	defer func(Body io.ReadCloser) {
		err = Body.Close()
		if err != nil {
			msg := fmt.Sprintf("error closing request body: %v", err)
			http.Error(w, msg, http.StatusInternalServerError)
			return
		}
	}(r.Body)

	data := dto.CreateWalletCommand{}
	err = json.Unmarshal(body, &data)
	if err != nil {
		msg := fmt.Sprintf("error parsing JSON: %v", err)
		http.Error(w, msg, http.StatusBadRequest)
		return
	}

	fmt.Printf("body: %v", data)

	sub, err := wc.JwtHelper.GetSubjectFromToken(r)

	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	wallet, err := wc.WalletService.CreateWallet(data, sub)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	err = json.NewEncoder(w).Encode(wallet)
	if err != nil {
		str := fmt.Sprintf("error encoding json for response: %v", err)
		http.Error(w, str, http.StatusInternalServerError)
	}
}

func (wc *WalletController) GetWalletById(w http.ResponseWriter, r *http.Request) {
	id := r.PathValue("id")
	wc.GetWalletByIdHandle(w, id)
}

func (wc *WalletController) GetWalletByIdHandle(w http.ResponseWriter, id string) {
	wallet, err := wc.WalletService.GetWalletById(id)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	err = json.NewEncoder(w).Encode(wallet)
	if err != nil {
		str := fmt.Sprintf("error encoding json for response: %v", err)
		http.Error(w, str, http.StatusInternalServerError)
	}
}
