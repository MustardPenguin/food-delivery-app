package api

import (
	"fmt"
	"log"
	"net/http"
	"payment-service/internal/api/controller"
)

func StartServer(port string) {
	setupController()

	addr := fmt.Sprintf(":%s", port)
	err := http.ListenAndServe(addr, nil)

	if err != nil {
		log.Fatalf("error running http server: %v", err)
	}
}

func setupController() {

	wc := controller.NewWalletController()

	http.HandleFunc("POST /api/v1/wallets", wc.CreateWallet)
}
