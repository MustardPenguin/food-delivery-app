package message

import (
	"fmt"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/confluentinc/confluent-kafka-go/schemaregistry"
	"github.com/confluentinc/confluent-kafka-go/schemaregistry/serde"
	"github.com/confluentinc/confluent-kafka-go/schemaregistry/serde/avro"
	"log"
)

func StartConsumers(config *kafka.ConfigMap, topics []string, schemaUrl string) {

	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		log.Fatalf("err starting consumer: %v", err)
	}

	client, err := schemaregistry.NewClient(schemaregistry.NewConfig(schemaUrl))
	if err != nil {
		log.Fatalf("err creating schema registry client: %v", err)
	}

	dser, err := avro.NewSpecificDeserializer(client, serde.ValueSerde, avro.NewDeserializerConfig())
	if err != nil {
		log.Fatalf("err creating deserializer: %v", err)
	}

	err = consumer.SubscribeTopics(topics, nil)
	if err != nil {
		log.Fatalf("err subscribing to topics: %v", err)
	}
	fmt.Printf("successfully started consumer, subscribed to topics: %v", topics)

	handleConsumer(consumer, dser)
}

func handleConsumer(consumer *kafka.Consumer, dser *avro.SpecificDeserializer) {
	for {
		msg, err := consumer.ReadMessage(-1)

		if err != nil {
			fmt.Printf("consumer err: %v", err)
			continue
		}
		fmt.Printf("msg: %v \n", msg)

		value := dto.Order{}
		err = dser.DeserializeInto(*msg.TopicPartition.Topic, msg.Value, &value)
		if err != nil {
			fmt.Printf("err deserializing msg: %v", err)
			continue
		}

		fmt.Printf("value: %v \n", value)
	}
}
