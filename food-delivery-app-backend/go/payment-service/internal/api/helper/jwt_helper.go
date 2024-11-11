package helper

import (
	"errors"
	"github.com/golang-jwt/jwt/v5"
	"net/http"
	"strings"
)

func GetSubjectFromToken(r *http.Request) (string, error) {
	token, err := extractJwt(r)
	if err != nil {
		return "", err
	}

	sub, err := extractSubject(token)
	if err != nil {
		return "", nil
	}

	return sub, nil
}

func extractJwt(r *http.Request) (string, error) {
	auth := r.Header.Get("Authorization")
	if auth == "" {
		return "", errors.New("no auth header found")
	}
	token := strings.Split(auth, " ")

	return token[1], nil
}

func extractSubject(jwtToken string) (string, error) {

	var sub string
	token, _, err := new(jwt.Parser).ParseUnverified(jwtToken, jwt.MapClaims{})

	if err != nil {
		return "", err
	}

	if claims, ok := token.Claims.(jwt.Claims); ok {
		sub, err = claims.GetSubject()
		if err != nil {
			return "", err
		}
	}
	return sub, nil
}
