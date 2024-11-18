package main

import (
	"food-delivery-app-backend/payment-service/internal/api"
	"github.com/joho/godotenv"
	"log"
	"os"
)

func main() {

	err := godotenv.Load("../.env")

	if err != nil {
		log.Fatalf("error loading .env files: %v", err)
	}

	port := os.Getenv("PORT")

	api.StartServer(port)
}
