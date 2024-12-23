package tests

import (
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/message-service/internal/application/port"
	"food-delivery-app-backend/message-service/internal/domain/entity"
	"food-delivery-app-backend/message-service/internal/infrastructure/repository"
	"testing"
	"time"
)

type MessageRepositoryTest struct {
	MessageRepository port.MessageRepository
	Message           entity.Message
}

func NewMessageRepositoryTest() *MessageRepositoryTest {
	return &MessageRepositoryTest{
		MessageRepository: repository.NewMessageSqlRepository(db),
		Message: entity.Message{
			MessageId: "d740b3ff-5e84-4ee8-83b5-17d61f291cef",
			AuthorId:  "cffe9967-73bc-4910-ab8c-0fb11e9567f3",
			OrderId:   "49d89e4c-13c7-4f68-ba94-76d7bac0d48c",
			Content:   "content message",
			SentAt:    time_util.Truncate(time.Now()),
		},
	}
}

func TestSaveMessage(t *testing.T) {
	mt := NewMessageRepositoryTest()

	message, err := mt.MessageRepository.Save(mt.Message)
	if err != nil {
		t.Errorf("error saving message: %v", err)
	}
	if message != mt.Message {
		t.Errorf("want %v got %v", mt.Message, message)
	}
}

func TestGetMessageById(t *testing.T) {
	mt := NewMessageRepositoryTest()

	message, err := mt.MessageRepository.GetById(mt.Message.MessageId)
	if err != nil {
		t.Errorf("error saving message: %v", err)
	}
	if message != mt.Message {
		t.Errorf("want %v got %v", mt.Message, message)
	}
}

func TestGetMessageByOrderId(t *testing.T) {
	mt := NewMessageRepositoryTest()
	orderId := "6ee03c6e-bda5-468f-94e3-ef50fe36046e"
	msgLength := 4

	msgs, err := mt.MessageRepository.GetByOrderId(orderId)
	if err != nil {
		t.Errorf("error getting messages: %v", msgs)
	}
	if len(msgs) != msgLength {
		t.Errorf("want %v got %v", msgLength, len(msgs))
	}
}
