package helper

import "net/http"

type JwtHelper interface {
	GetSubjectFromToken(r *http.Request) (string, error)
}
