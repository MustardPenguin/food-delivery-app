package main

import (
	"food-delivery-app-backend/libs/db_util"
	"food-delivery-app-backend/message-service/internal/api"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"log"
)

func main() {

	err := godotenv.Load("../.env")
	if err != nil {
		log.Fatalf("error loading .env files: %v", err)
	}

	db := db_util.InitConnection()
	db_util.ExecuteScript(db, "./script/init-schema.sql")

	api.StartServer("8186", db)
}
