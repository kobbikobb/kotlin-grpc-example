package com.example.greeting

import com.example.greeting.v1.GreetRequest
import com.example.greeting.v1.GreetResponse
import com.example.greeting.v1.GreetingServiceGrpcKt.GreetingServiceCoroutineImplBase

class GreetingServiceImpl : GreetingServiceCoroutineImplBase() {
    override suspend fun greet(request: GreetRequest): GreetResponse {
        val message = "Greetings, ${request.name}!"
        return GreetResponse.newBuilder().setMessage(message).build()
    }
}
