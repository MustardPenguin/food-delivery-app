package main

import (
	"github.com/joho/godotenv"
	"log"
	"os"
	"payment-service/internal/api"
)

func main() {

	err := godotenv.Load("../.env")

	if err != nil {
		log.Fatalf("error loading .env files: %v", err)
	}

	port := os.Getenv("PORT")

	api.StartServer(port)
}
