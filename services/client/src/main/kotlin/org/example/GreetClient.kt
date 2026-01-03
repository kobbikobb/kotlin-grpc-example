package org.example.client

import com.example.greeting.v1.GreetRequest
import com.example.greeting.v1.GreetingServiceGrpcKt.GreetingServiceCoroutineStub
import io.grpc.ManagedChannel

class GreetingClient(
    private val channel: ManagedChannel,
    private val port: Int,
) {
    suspend fun sendGreet(greet: String) {
        val greeter = GreetingServiceCoroutineStub(channel)
        val response =
            greeter.greet(
                GreetRequest.newBuilder().setName(greet).build(),
            )
        println("Response from server: ${response.message}")
    }
}
