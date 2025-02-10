package adapter

import (
	"database/sql"
	"errors"
	"food-delivery-app-backend/libs/time_util"
	"food-delivery-app-backend/message-service/internal/application/dto"
	"food-delivery-app-backend/message-service/internal/application/port"
	"food-delivery-app-backend/message-service/internal/domain/entity"
	"food-delivery-app-backend/message-service/internal/infrastructure/repository"
	"github.com/google/uuid"
)

type DefaultMessageService struct {
	MessageRepository port.MessageRepository
}

func NewDefaultMessageService(db *sql.DB) *DefaultMessageService {
	return &DefaultMessageService{
		MessageRepository: repository.NewMessageSqlRepository(db),
	}
}

func (m *DefaultMessageService) CreateMessage(cmd dto.SendMessageCommand, authorId string) (entity.Message, error) {
	if cmd.Content == "" {
		return entity.Message{}, errors.New("empty content")
	}
	msg := entity.Message{
		MessageId: uuid.NewString(),
		AuthorId:  authorId,
		OrderId:   cmd.OrderId,
		Content:   cmd.Content,
		SentAt:    time_util.GetCurrentTime(),
	}

	saved, err := m.MessageRepository.Save(msg)
	if err != nil {
		return entity.Message{}, err
	}

	return saved, nil
}
