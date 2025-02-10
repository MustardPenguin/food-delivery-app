package main

import (
	"food-delivery-app-backend/libs/db_util"
	"food-delivery-app-backend/libs/log_util"
	"food-delivery-app-backend/payment-service/internal/api"
	"food-delivery-app-backend/payment-service/internal/infrastructure/message"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"log"
	"os"
)

func main() {

	err := godotenv.Load("../.env")

	if err != nil {
		log.Fatalf("error loading .env files: %v", err)
	}

	schemaUrl := os.Getenv("SCHEMA_REGISTRY_HOST")
	port := os.Getenv("PORT")

	config := &kafka.ConfigMap{
		"bootstrap.servers": os.Getenv("KAFKA_BOOTSTRAP_SERVERS"),
		"group.id":          "payment-service-group",
		"auto.offset.reset": "earliest",
	}

	db := db_util.InitConnection()
	db_util.ExecuteScript(db, "./script/init-schema.sql")

	orderCreated := os.Getenv("ORDER_CREATED_EVENT_TOPIC")
	topics := []string{orderCreated}

	logger := log_util.InitLogging()

	go message.StartConsumers(config, topics, schemaUrl, db, logger)
	go api.StartServer(port, db)
	select {}
}
