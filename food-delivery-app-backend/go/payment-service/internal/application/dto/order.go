package dto

import "google.golang.org/genproto/googleapis/type/date"

type Order struct {
	OrderId      string
	CustomerId   string
	RestaurantId string
	Address      string
	OrderedAt    date.Date
	OrderStatus  string
	TotalPrice   float64
}
