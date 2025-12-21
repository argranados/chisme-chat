#!/bin/bash
set -e

echo "=== Cleaning MySQL before smoke tests ==="
docker exec -i mysql-db mysql -uuser -ppassword mydb < performance/cleanup.sql

echo "=== Running smoke tests ==="
"/c/ProgramData/chocolatey/bin/k6.exe" run performance/smoke-e2e.js