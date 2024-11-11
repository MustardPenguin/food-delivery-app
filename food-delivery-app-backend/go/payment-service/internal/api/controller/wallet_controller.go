package controller

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"payment-service/internal/api/helper"
	"payment-service/internal/application/dto"
)

type WalletController struct {
}

func NewWalletController() WalletController {
	return WalletController{}
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

	sub, err := helper.GetSubjectFromToken(r)

	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
	}
}
