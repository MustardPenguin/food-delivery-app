package port

import (
	"food-delivery-app-backend/message-service/internal/application/dto"
	"food-delivery-app-backend/message-service/internal/domain/entity"
)

type MessageService interface {
	CreateMessage(msg dto.SendMessageCommand, authorId string) (entity.Message, error)
}
