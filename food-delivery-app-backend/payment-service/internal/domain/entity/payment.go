package entity

type Payment struct {
	PaymentId  string
	OrderId    string
	CustomerId string
	Amount     float64
}
