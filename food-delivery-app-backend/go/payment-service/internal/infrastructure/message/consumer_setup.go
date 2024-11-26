package message

import (
	"database/sql"
	"fmt"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"log"
)

func StartConsumers(config *kafka.ConfigMap, topics []string, schemaUrl string, db *sql.DB) {

	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		log.Fatalf("err starting consumer: %v", err)
	}

	err = consumer.SubscribeTopics(topics, nil)
	if err != nil {
		log.Fatalf("err subscribing to topics: %v", err)
	}
	fmt.Printf("successfully started consumer, subscribed to topics: %v", topics)

	handleConsumer(consumer, schemaUrl, db)
}

func handleConsumer(consumer *kafka.Consumer, schemaUrl string, db *sql.DB) {
	handler := NewMessageHandler(db)
	for {
		msg, err := consumer.ReadMessage(-1)

		if err != nil {
			fmt.Printf("consumer err: %v", err)
			continue
		}

		err = handler.HandleMessage(msg, schemaUrl)
		if err != nil {
			log.Printf("error handling message: %v", err)
			continue
		}
	}
}
