package controller

import (
	"database/sql"
	"encoding/json"
	"food-delivery-app-backend/libs/controller_util"
	"food-delivery-app-backend/libs/jwt_util"
	"food-delivery-app-backend/message-service/internal/application/adapter"
	"food-delivery-app-backend/message-service/internal/application/dto"
	"food-delivery-app-backend/message-service/internal/application/port"
	"log"
	"net/http"
)

type MessageController struct {
	MessageService port.MessageService
}

func NewMessageController(db *sql.DB) *MessageController {
	return &MessageController{
		MessageService: adapter.NewDefaultMessageService(db),
	}
}

func (mc *MessageController) CreateMessage(w http.ResponseWriter, r *http.Request) {
	msgCmd, err := controller_util.GetRequestBody(r, dto.SendMessageCommand{})
	if err != nil {
		log.Printf("err while getting body: %v", err)
		return
	}
	// Not going to test this so no DI
	sub, err := jwt_util.NewJwtHelperImpl().GetSubjectFromToken(r)

	if err != nil {
		log.Printf("err extracting subject: %v", err)
		return
	}

	response, err := mc.MessageService.CreateMessage(msgCmd, sub)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	log.Printf("Response: %s", response)
	log.Printf("body: %v", msgCmd)
	json.NewEncoder(w).Encode(response)
}
