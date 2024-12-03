package dto

type PaymentRequest struct {
	CustomerId string
	WalletId   string
	OrderId    string
	Amount     float64
}
