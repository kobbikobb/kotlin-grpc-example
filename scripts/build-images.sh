#!/bin/bash

docker build \
    -f services/server/Dockerfile \
    -t greeting-server:latest \
    --progress=plain \
    .
