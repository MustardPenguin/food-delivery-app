package message

import (
	"encoding/binary"
	"encoding/json"
	"fmt"
	common "food-delivery-app-backend/common/api"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"food-delivery-app-backend/payment-service/internal/domain/entity"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/linkedin/goavro"
	"net/http"
	"time"
)

type MessageHandler struct {
	PaymentService port.PaymentService
}

func NewMessageHandler() *MessageHandler {
	return &MessageHandler{
		PaymentService: adapter.NewStandardPaymentService(),
	}
}

func (m *MessageHandler) HandleMessage(msg *kafka.Message, schemaUrl string) error {

	schemaId := int(binary.BigEndian.Uint32(msg.Value[1:5]))
	schema, err := getSchema(schemaUrl, schemaId)
	if err != nil {
		return err
	}
	val, err := decodeAvro(msg.Value, schema)
	if err != nil {
		return err
	}

	payment, err := eventToPayment(val)
	if err != nil {
		return err
	}
	err = m.PaymentService.PayOrder(payment)
	if err != nil {
		return err
	}

	return nil
}

func eventToPayment(msg interface{}) (entity.Payment, error) {

	ev := msg.(map[string]interface{})
	after := ev["after"].(map[string]interface{})
	value := after["order_created.order_command.order_created_events.Value"].(map[string]interface{})
	payload := fmt.Sprintf("%v", value["payload"])

	var order map[string]interface{}
	err := json.Unmarshal([]byte(payload), &order)
	if err != nil {
		fmt.Printf("error unmarshalling payload: %v", err)
		return entity.Payment{}, err
	}

	payment := entity.Payment{
		CustomerId: order["customerId"].(string),
		WalletId:   order["walletId"].(string),
		OrderId:    order["orderId"].(string),
		Amount:     order["totalPrice"].(float64),
		CreatedAt:  time.Now().UTC(),
	}

	fmt.Printf("\n payment: %v", payment)
	return payment, nil
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
