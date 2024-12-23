package db_util

import (
	"database/sql"
	"fmt"
	"log"
	"os"
)

func InitConnection() *sql.DB {
	// Connect to database
	dbHost := os.Getenv("DATABASE_HOST")
	dbUser := os.Getenv("POSTGRES_USER")
	dbPort := os.Getenv("DATABASE_PORT")
	dbPassword := os.Getenv("POSTGRES_PASSWORD")

	dbUrl := fmt.Sprintf("postgres://%s:%s@%s:%s/%s?sslmode=disable",
		dbUser, dbPassword, dbHost, dbPort, "postgres")

	db, err := sql.Open("postgres", dbUrl)
	if err != nil {
		log.Fatalf("Error connecting to database: %v", err)
	}

	return db
}

func ExecuteScript(db *sql.DB, path string) {
	script, err := os.ReadFile(path)
	if err != nil {
		log.Fatalf("Error reading file: %v", err)
	}

	_, err = db.Exec(string(script))
	if err != nil {
		log.Fatalf("Error running script: %v", err)
	}
}
