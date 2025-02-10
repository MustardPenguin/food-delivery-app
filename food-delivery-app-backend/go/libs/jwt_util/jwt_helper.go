package jwt_util

import "net/http"

type JwtHelper interface {
	GetSubjectFromToken(r *http.Request) (string, error)
}
