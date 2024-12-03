
# Variables
DEBEZIUM_API=http://localhost:8083/connectors
POSTGRES_CONTAINER_NAME=postgres
DATABASE_HOSTNAME=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' $POSTGRES_CONTAINER_NAME)
DATABASE_USER="user"
DATABASE_PASSWORD="admin"
SLEEP_TIME=1

echo "Postgres IP Address: $DATABASE_HOSTNAME"

# Delete current connectors
curl -X DELETE "${DEBEZIUM_API}/order-created-events-connector";
curl -X DELETE "${DEBEZIUM_API}/payment-created-events-connector"

# Json config
order_created_json=$(jq -n \
  --arg dbh "$DATABASE_HOSTNAME" \
  --arg user "$DATABASE_USER" \
  --arg password "$DATABASE_PASSWORD" \
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

payment_created_json=$(jq -n \
  --arg dbh "$DATABASE_HOSTNAME" \
  --arg user "$DATABASE_USER" \
  --arg password "$DATABASE_PASSWORD" \
  '{
    "name": "payment-created-events-connector",
    "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "tasks.max": "1",
      "database.hostname": $dbh,
      "database.user": $user,
      "database.password": $password,
      "database.dbname": "postgres",
      "table.include.list": "payment.payment_created_events",
      "topic.prefix": "payment_created",
      "tombstones.on.delete" : "false",
      "slot.name": "payment_created_slot",
      "plugin.name": "pgoutput",
    }
  }')


curl -X POST -H "Content-Type: application/json" --data "$order_created_json" $DEBEZIUM_API
sleep $SLEEP_TIME;
curl -X POST -H "Content-Type: application/json" --data "$payment_created_json" $DEBEZIUM_API
sleep $SLEEP_TIME;

printf "\n"
printf "Connectors created: \n"
#curl http://localhost:8083/connectors
curl $DEBEZIUM_API 
