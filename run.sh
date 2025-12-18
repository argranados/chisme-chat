#!/bin/bash

# Detener ejecución si ocurre algún error
set -e

echo "🔹 Limpiando y empaquetando con Maven..."
mvn clean package -DskipTests

echo "🔹 Reconstruyendo imágenes Docker..."
docker-compose build

echo "🔹 Levantando contenedores..."
docker-compose up