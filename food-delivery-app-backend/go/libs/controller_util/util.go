package controller_util

import (
	"encoding/json"
	"io"
	"log"
	"net/http"
)

func ConvertToJson(body map[string]interface{}) ([]byte, error) {
	j, err := json.Marshal(body)

	if err != nil {
		log.Printf("error while parsing json: %v", err)
		return nil, err
	}

	return j, nil
}

func GetResponseBody[T any](res *http.Response, data T) (T, error) {
	body, err := io.ReadAll(res.Body)
	if err != nil {
		log.Printf("Unable to read response body: %v", http.StatusInternalServerError)
		return data, err
	}
	defer res.Body.Close()

	err = json.Unmarshal(body, &data)
	if err != nil {
		log.Printf("Error parsing json: %v", err)
		return data, err
	}

	return data, nil
}

func GetRequestBody[T any](r *http.Request, data T) (T, error) {
	body, err := io.ReadAll(r.Body)
	if err != nil {
		log.Printf("err reading request body: %v", err)
		return data, err
	}
	defer r.Body.Close()
	err = json.Unmarshal(body, &data)
	if err != nil {
		log.Printf("Error parsing json: %v", err)
		return data, err
	}

	return data, nil
}