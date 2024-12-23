package entity

import "time"

type Message struct {
	MessageId string
	AuthorId  string
	OrderId   string
	Content   string
	SentAt    time.Time
}
