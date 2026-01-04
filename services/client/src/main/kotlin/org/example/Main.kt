package org.example.client

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

suspend fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide at least one name to greet.")
        return
    }

    val serverHost = System.getenv("GRPC_SERVER_HOST") ?: "localhost"
    val port = System.getenv("GRPC_SERVER_PORT")?.toIntOrNull() ?: 50051

    val channel: ManagedChannel =
        ManagedChannelBuilder
            .forAddress(serverHost, port)
            .usePlaintext()
            .build()

    val app = GreetingClient(channel, port)

    for (greet in args) {
        app.sendGreet(greet)
    }

    channel.shutdown()
}
