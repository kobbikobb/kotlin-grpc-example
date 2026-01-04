#!/bin/bash

docker run \
    --name greeting-server \
    --rm \
    -p 50051:50051 \
    greeting-server:latest
