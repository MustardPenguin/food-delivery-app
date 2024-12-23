package main

import (
	"food-delivery-app-backend/libs/db_util"
	"food-delivery-app-backend/payment-service/internal/api"
	"food-delivery-app-backend/payment-service/internal/infrastructure/message"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/joho/godotenv"
	_ "github.com/lib/pq"
	"github.com/rs/zerolog"
	"io"
	"log"
	"net"
	"os"
	"time"
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
	db_util.ExecuteScript(db, "./script/init-schema-test.sql")

	orderCreated := os.Getenv("ORDER_CREATED_EVENT_TOPIC")
	topics := []string{orderCreated}

	logger := initLogging()

	go message.StartConsumers(config, topics, schemaUrl, db, logger)
	go api.StartServer(port, db)
	select {}
}

func initLogging() zerolog.Logger {
	logger := zerolog.New(os.Stdout).With().
		Timestamp().Str("golang-service", "payment-service").Logger()
	logstashHost := os.Getenv("LOGSTASH_HOST")
	conn, err := net.Dial("tcp", logstashHost)
	if err != nil {
		logger.Info().Msgf("error connecting to logstash host: %v, defaulting to zerolog without log aggregation", err)
		return logger
	}
	consoleWriter := zerolog.ConsoleWriter{Out: os.Stdout, TimeFormat: time.RFC3339}
	multiWriter := io.MultiWriter(consoleWriter, conn)

	logger = zerolog.New(multiWriter).With().
		Timestamp().Str("golang-service", "payment-service").Logger()
	logger.Info().Msg("successfully connected to logstash host for log aggregation")
	return logger
}
