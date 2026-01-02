package org.example

import com.example.greeting.GreetingServiceImpl
import com.example.greeting.v1.GreetRequest
import com.example.greeting.v1.GreetResponse
import io.grpc.Server
import io.grpc.ServerBuilder

class App(
    private val port: Int,
    private val greetingService: GreetingServiceImpl,
) {
    private val server: Server =
        ServerBuilder
            .forPort(port)
            .addService(greetingService)
            .build()

    fun start() {
        server.start()
        println("Server started, listening on $port")

        Runtime.getRuntime().addShutdownHook(
            Thread {
                println("Shutting down...")
                server.shutdown()
                println("Server shut down.")
            },
        )
    }

    fun blockUntilShutdown() {
        server.awaitTermination()
    }
}

fun main() {
    var port = 50051
    var greetingService = GreetingServiceImpl()

    var app = App(port, greetingService)
    app.start()
    app.blockUntilShutdown()
}
