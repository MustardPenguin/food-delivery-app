package valueobject

type PaymentStatus string

const (
	COMPLETED PaymentStatus = "completed"
	REFUNDED                = "refunded"
	CANCELED                = "canceled"
	FAILED                  = "failed"
)
