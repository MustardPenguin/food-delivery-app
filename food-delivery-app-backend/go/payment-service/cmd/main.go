package main

import (
	"database/sql"
	"fmt"
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

	db := initConnection()
	orderCreated := os.Getenv("ORDER_CREATED_EVENT_TOPIC")
	topics := []string{orderCreated}

	message.StartConsumers(config, topics, schemaUrl)
	api.StartServer(port, db)
}

func initConnection() *sql.DB {
	// Connect to database
	dbHost := os.Getenv("DATABASE_HOST")
	dbUser := os.Getenv("POSTGRES_USER")
	dbPort := os.Getenv("DATABASE_PORT")
	dbPassword := os.Getenv("POSTGRES_PASSWORD")

	dbUrl := fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=disable",
		dbUser, dbPassword, dbHost, dbPort, "postgres")

	db, err := sql.Open("postgres", dbUrl)
	if err != nil {
		log.Fatalf("Error connecting to database: %v", err)
	}

	runScript(db, "./script/init-schema.sql")
	return db
}

func runScript(db *sql.DB, path string) {
	script, err := os.ReadFile(path)
	if err != nil {
		log.Fatalf("Error reading file: %v", err)
	}

	_, err = db.Exec(string(script))
	if err != nil {
		log.Fatalf("Error running script: %v", err)
	}
}
