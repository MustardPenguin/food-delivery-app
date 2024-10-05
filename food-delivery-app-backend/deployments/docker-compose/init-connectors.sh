
# Variables
DEBEZIUM_API=http://localhost:8083/connectors

POSTGRES_CONTAINER_NAME=postgres
APICURIO_CONTAINER_NAME=apicurio

DATABASE_HOSTNAME=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $POSTGRES_CONTAINER_NAME)
APICURIO_HOSTNAME=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $APICURIO_CONTAINER_NAME)
APICURIO_API="http://${APICURIO_HOSTNAME}:8080/"

DATABASE_USER="user"
DATABASE_PASSWORD="admin"
SLEEP_TIME=1

echo "Postgres IP Address: $DATABASE_HOSTNAME"
echo "Apicurio API: $APICURIO_API"

# Json config
order_created_json=$(jq -n \
  --arg dbh "$DATABASE_HOSTNAME" \
  --arg user "$DATABASE_USER" \
  --arg password "$DATABASE_PASSWORD" \
  --arg apicurio_api "$APICURIO_API" \
  '{
    "name": "order-created-events-connector",
    "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": $dbh,
      "database.user": $user,
      "database.password": $password,
      "database.dbname": "postgres",
      "table.include.list": "order_command.order_created_events",
      "topic.prefix": "order_created",
      "tombstones.on.delete" : "false",
      "slot.name": "order_created_slot",
      "plugin.name": "pgoutput",
    }
  }')

curl -X POST -H "Content-Type: application/json" --data "$order_created_json" $DEBEZIUM_API
sleep $SLEEP_TIME;

printf "\n"
printf "Connectors created: \n"
curl http://localhost:8083/connectors