#!/usr/bin/env bash
# Start the backend in development mode (requires local MySQL).
set -e

cd "$(dirname "$0")/../backend"
echo "Starting BookStore backend (dev profile)..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
