package org.example.server

import com.example.server.GreetingServiceImpl
import org.example.server.GreetServer

fun main() {
    var port = 50051
    var greetingService = GreetingServiceImpl()

    var server = GreetServer(port, greetingService)
    server.start()
    server.blockUntilShutdown()
}
