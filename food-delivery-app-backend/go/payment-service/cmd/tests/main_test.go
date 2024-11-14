package tests

import (
	"context"
	"database/sql"
	_ "github.com/lib/pq"
	"log"
	"os"
	"payment-service/cmd/tests/setup"
	"testing"
)

var db *sql.DB

func TestMain(m *testing.M) {

	ctx := context.Background()
	pgContainer, database := setup.Startup(ctx)

	if err := database.Ping(); err != nil {
		log.Fatalf("db.Ping() failed: %v", err)
	}

	defer func() {
		if err := pgContainer.Terminate(ctx); err != nil {
			log.Fatalf("failed to terminate container: %s", err)
		}
	}()

	defer func(database *sql.DB) {
		err := database.Close()
		if err != nil {
			log.Fatalf("Error closing db")
		}
	}(database)

	db = database

	code := m.Run()

	os.Exit(code)
}
