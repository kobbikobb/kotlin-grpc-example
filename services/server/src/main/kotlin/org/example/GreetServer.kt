package org.example.server

import com.example.greeting.v1.GreetRequest
import com.example.greeting.v1.GreetResponse
import com.example.server.GreetingServiceImpl
import io.grpc.Server
import io.grpc.ServerBuilder

class GreetServer(
    private val port: Int,
    private val greetingService: GreetingServiceImpl,
) {
    private val server: Server =
        ServerBuilder
            .forPort(port)
            .addService(greetingService)
            .build()

    fun start() {
        println("Starting server...")
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
