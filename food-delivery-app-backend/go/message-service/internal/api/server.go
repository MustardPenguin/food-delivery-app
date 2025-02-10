package api

import (
	"database/sql"
	"fmt"
	"food-delivery-app-backend/message-service/internal/api/controller"
	"log"
	"net/http"
)

func StartServer(port string, db *sql.DB) {

	mc := controller.NewMessageController(db)
	http.HandleFunc("POST /api/v1/messages", mc.CreateMessage)

	addr := fmt.Sprintf(":%s", port)
	err := http.ListenAndServe(addr, nil)

	if err != nil {
		log.Fatalf("error running http server: %v", err)
	}
}
