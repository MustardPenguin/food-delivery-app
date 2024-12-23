package cmd

import (
	"food-delivery-app-backend/libs/db_util"
	"github.com/joho/godotenv"
	"log"
)

func main() {

	err := godotenv.Load("../.env")
	if err != nil {
		log.Fatalf("error loading .env files: %v", err)
	}

	db := db_util.InitConnection()
	db_util.ExecuteScript(db, "./script/init-schema-test.sql")

}
