#!/bin/bash

export DOCKER_BUILDKIT=1

docker build \
    -f services/server/Dockerfile \
    -t greeting-server:latest \
    --progress=plain \
    .

docker build \
    -f services/client/Dockerfile \
    -t greeting-client:latest \
    --progress=plain \
    .
