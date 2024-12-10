package message

import (
	"database/sql"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/rs/zerolog"
)

func StartConsumers(config *kafka.ConfigMap, topics []string, schemaUrl string, db *sql.DB, log zerolog.Logger) {

	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		log.Fatal().Msgf("err starting consumer: %v", err)
	}

	err = consumer.SubscribeTopics(topics, nil)
	if err != nil {
		log.Fatal().Msgf("err subscribing to topics: %v", err)
	}
	log.Info().Msgf("successfully started consumer, subscribed to topics: %v", topics)

	handleConsumer(consumer, schemaUrl, db, log)
}

func handleConsumer(consumer *kafka.Consumer, schemaUrl string, db *sql.DB, log zerolog.Logger) {
	handler := NewMessageHandler(db, log)
	for {
		msg, err := consumer.ReadMessage(-1)

		if err != nil {
			log.Error().Msgf("consumer err: %v", err)
			continue
		}

		err = handler.HandleMessage(msg, schemaUrl)
		if err != nil {
			log.Error().Msgf("error handling message: %v", err)
			continue
		}
	}
}
