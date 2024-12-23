package port

import "food-delivery-app-backend/message-service/internal/domain/entity"

type MessageRepository interface {
	Save(msg entity.Message) (entity.Message, error)
	GetById(id string) (entity.Message, error)
	GetByOrderId(id string) ([]entity.Message, error)
}
