#!/bin/sh
set -e

DB_HOST=${DB_HOST:-mysql}
DB_PORT=${DB_PORT:-3306}

echo "Waiting for database ${DB_HOST}:${DB_PORT}..."

# wait for TCP socket to be open
while ! nc -z "$DB_HOST" "$DB_PORT"; do
  sleep 1
done

echo "Database ${DB_HOST}:${DB_PORT} is available — starting application"

exec java -jar app.jar

