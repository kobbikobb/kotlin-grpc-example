package org.example.client

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

suspend fun main() {
    var port = 50051

    val channel: ManagedChannel =
        ManagedChannelBuilder
            .forAddress("localhost", port)
            .usePlaintext()
            .build()

    val app = GreetingClient(channel, port)

    println("Type 'exit' to quit or press Enter to send a greeting...")
    while (true) {
        val input = readLine()

        if (input == null || input.isBlank()) continue
        if (input.lowercase() == "exit") break

        app.sendGreet(input)
    }

    channel.shutdown()
}
