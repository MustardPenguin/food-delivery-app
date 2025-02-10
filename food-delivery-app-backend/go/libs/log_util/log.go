package log_util

import (
	"github.com/rs/zerolog"
	"io"
	"net"
	"os"
	"time"
)

func InitLogging() zerolog.Logger {
	logger := zerolog.New(os.Stdout).With().
		Timestamp().Str("golang-service", "payment-service").Logger()
	logstashHost := os.Getenv("LOGSTASH_HOST")
	conn, err := net.Dial("tcp", logstashHost)
	if err != nil {
		logger.Info().Msgf("error connecting to logstash host: %v, defaulting to zerolog without log aggregation", err)
		return logger
	}
	consoleWriter := zerolog.ConsoleWriter{Out: os.Stdout, TimeFormat: time.RFC3339}
	multiWriter := io.MultiWriter(consoleWriter, conn)

	logger = zerolog.New(multiWriter).With().
		Timestamp().Str("golang-service", "payment-service").Logger()
	logger.Info().Msg("successfully connected to logstash host for log aggregation")
	return logger
}
