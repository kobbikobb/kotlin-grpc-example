#!/bin/bash

# Create network if it doesn't exist
docker network create greeting-network 2>/dev/null || true

docker run \
    --name greeting-server \
    --network greeting-network \
    -p 50051:50051 \
    -d \
    greeting-server:latest

# Give the server a moment to start
sleep 2

docker run \
    --name greeting-client \
    --network greeting-network \
    --rm \
    -e GRPC_SERVER_HOST=greeting-server \
    greeting-client:latest "Jakob, how is sending a greeting from the client!"

docker stop greeting-server
docker rm greeting-server
