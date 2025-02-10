package message

import (
	"database/sql"
	"encoding/binary"
	"encoding/json"
	"fmt"
	common "food-delivery-app-backend/libs/controller_util"
	"food-delivery-app-backend/payment-service/internal/application/adapter"
	"food-delivery-app-backend/payment-service/internal/application/dto"
	"food-delivery-app-backend/payment-service/internal/application/port"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/linkedin/goavro"
	"github.com/rs/zerolog"
	"net/http"
)

type MessageHandler struct {
	PaymentService port.PaymentService
	log            zerolog.Logger
}

func NewMessageHandler(db *sql.DB, log zerolog.Logger) *MessageHandler {
	return &MessageHandler{
		PaymentService: adapter.NewStandardPaymentService(db),
		log:            log,
	}
}

func (m *MessageHandler) HandleMessage(msg *kafka.Message, schemaUrl string) error {

	schemaId := int(binary.BigEndian.Uint32(msg.Value[1:5]))
	schema, err := getSchema(schemaUrl, schemaId)
	if err != nil {
		m.log.Error().Msgf("error getting schema, %v", err)
		return err
	}
	val, err := decodeAvro(msg.Value, schema)
	if err != nil {
		m.log.Error().Msgf("error decoding avro message, %v", err)
		return err
	}

	req, err := eventToPayment(val)
	if err != nil {
		m.log.Error().Msgf("error converting event to payment: %v", err)
		return err
	}
	_, err = m.PaymentService.PayOrder(req)
	if err != nil {
		m.log.Error().Msgf("error processing order payment for order id %v: %v", req.OrderId, err)
		return err
	}

	return nil
}

func eventToPayment(msg interface{}) (dto.PaymentRequest, error) {

	ev := msg.(map[string]interface{})
	after := ev["after"].(map[string]interface{})
	value := after["order_created.order_command.order_created_events.Value"].(map[string]interface{})
	payload := fmt.Sprintf("%v", value["payload"])

	var order map[string]interface{}
	err := json.Unmarshal([]byte(payload), &order)
	if err != nil {
		fmt.Printf("error unmarshalling payload: %v", err)
		return dto.PaymentRequest{}, err
	}

	req := dto.PaymentRequest{
		CustomerId: order["customerId"].(string),
		WalletId:   order["walletId"].(string),
		OrderId:    order["orderId"].(string),
		Amount:     order["totalPrice"].(float64),
	}

	fmt.Printf("\n payment: %v", req)
	return req, nil
}

func getSchema(schemaUrl string, id int) (string, error) {
	url := fmt.Sprintf("%s/schemas/ids/%d", schemaUrl, id)
	res, err := http.Get(url)

	if err != nil {
		return "", err
	}
	defer res.Body.Close()

	var data map[string]interface{}
	schema, err := common.GetResponseBody(res, data)
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
