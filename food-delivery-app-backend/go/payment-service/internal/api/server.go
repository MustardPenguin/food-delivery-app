package api

import (
	"database/sql"
	"fmt"
	"food-delivery-app-backend/payment-service/internal/api/controller"
	"log"
	"net/http"
)

func StartServer(port string, db *sql.DB) {
	setupController(db)

	addr := fmt.Sprintf(":%s", port)
	err := http.ListenAndServe(addr, nil)

	if err != nil {
		log.Fatalf("error running http server: %v", err)
	}
}

func setupController(db *sql.DB) {

	wc := controller.NewWalletController(db)

	http.HandleFunc("POST /api/v1/wallets", wc.CreateWallet)
}
