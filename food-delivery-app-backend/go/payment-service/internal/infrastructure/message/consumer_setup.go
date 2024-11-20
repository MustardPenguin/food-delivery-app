package message

import (
	"encoding/binary"
	"fmt"
	common "food-delivery-app-backend/common/api"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/linkedin/goavro"
	"log"
	"net/http"
)

func StartConsumers(config *kafka.ConfigMap, topics []string, schemaUrl string) {

	consumer, err := kafka.NewConsumer(config)
	if err != nil {
		log.Fatalf("err starting consumer: %v", err)
	}

	//client, err := schemaregistry.NewClient(schemaregistry.NewConfig(schemaUrl))
	//if err != nil {
	//	log.Fatalf("err creating schema registry client: %v", err)
	//}
	//
	//dser, err := avro.NewSpecificDeserializer(client, serde.ValueSerde, avro.NewDeserializerConfig())
	//if err != nil {
	//	log.Fatalf("err creating deserializer: %v", err)
	//}

	err = consumer.SubscribeTopics(topics, nil)
	if err != nil {
		log.Fatalf("err subscribing to topics: %v", err)
	}
	fmt.Printf("successfully started consumer, subscribed to topics: %v", topics)

	handleConsumer(consumer, schemaUrl)
}

func handleConsumer(consumer *kafka.Consumer, schemaUrl string) {
	for {
		msg, err := consumer.ReadMessage(-1)

		if err != nil {
			fmt.Printf("consumer err: %v", err)
			continue
		}

		fmt.Printf("msg: %v \n", msg)
		schemaId := int(binary.BigEndian.Uint32(msg.Value[1:5]))
		schema, err := getSchema(schemaUrl, schemaId)
		if err != nil {
			fmt.Printf("error getting schema: %v", schema)
			continue
		}
		val, err := decodeAvro(msg.Value, schema)
		if err != nil {
			fmt.Printf("error decoding avro: %v", err)
			continue
		}
		fmt.Printf("val: %v", val)
	}
}

func getSchema(schemaUrl string, id int) (string, error) {
	url := fmt.Sprintf("%s/schemas/ids/%d", schemaUrl, id)
	res, err := http.Get(url)

	if err != nil {
		return "", err
	}
	defer res.Body.Close()

	var data map[string]interface{}
	schema, err := common.GetBody(res, data)
	if err != nil {
		return "", err
	}

	return schema["schema"].(string), nil
}

func decodeAvro(data []byte, schema string) (interface{}, error) {

	codec, err := goavro.NewCodec(schema)
	if err != nil {
		return nil, err
	}
	decoded, _, err := codec.NativeFromBinary(data[5:])
	if err != nil {
		return nil, err
	}

	return decoded, nil
}
