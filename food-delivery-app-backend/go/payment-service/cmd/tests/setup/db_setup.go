package setup

import (
	"context"
	"database/sql"
	_ "github.com/lib/pq"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/modules/postgres"
	"github.com/testcontainers/testcontainers-go/wait"
	"log"
	"os"
	"time"
)

var dbUser string = "user"
var dbPassword string = "password"

func setupDbContainer(ctx context.Context) *postgres.PostgresContainer {

	postgresContainer, err := postgres.Run(
		ctx,
		"postgres:latest",
		postgres.WithDatabase("postgres"),
		postgres.WithUsername(dbUser),
		postgres.WithPassword(dbPassword),
		testcontainers.WithWaitStrategy(
			wait.ForLog("database system is ready to accept connections").
				WithOccurrence(2).
				WithStartupTimeout(5*time.Second),
		))

	if err != nil {
		log.Fatalf("Error starting container: %v", err)
	}

	return postgresContainer
}

func connectToDb(pgContainer *postgres.PostgresContainer, ctx context.Context) *sql.DB {

	_, err := pgContainer.Host(ctx)

	if err != nil {
		log.Fatalf("error getting db host: %v", err)
	}

	connectionString, err := pgContainer.ConnectionString(ctx, "sslmode=disable")

	if err != nil {
		log.Fatalf("error getting connection string: %v", err)
	}

	db, err := sql.Open("postgres", connectionString)
	if err != nil {
		log.Fatalf("error connecting do database: %v", err)
	}
	return db
}

func executeScript(db *sql.DB) {
	script, err := os.ReadFile("./setup/init-schema-test.sql")
	if err != nil {
		log.Fatalf("Error reading file: %v", err)
	}

	_, err = db.Exec(string(script))
	if err != nil {
		log.Fatalf("Error running script: %v", err)
	}
}

func Startup(ctx context.Context) (*postgres.PostgresContainer, *sql.DB) {

	pgContainer := setupDbContainer(ctx)
	db := connectToDb(pgContainer, ctx)
	executeScript(db)

	return pgContainer, db
}
