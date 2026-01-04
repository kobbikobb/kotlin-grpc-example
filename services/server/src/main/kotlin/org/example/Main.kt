package org.example.server

import com.example.server.GreetingServiceImpl
import org.example.server.GreetServer

fun main() {
    val port = System.getenv("GRPC_SERVER_PORT")?.toIntOrNull() ?: 50051
    var greetingService = GreetingServiceImpl()

    var server = GreetServer(port, greetingService)
    server.start()
    server.blockUntilShutdown()
}
