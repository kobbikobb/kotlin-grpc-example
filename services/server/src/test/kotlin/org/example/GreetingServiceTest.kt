package org.example.server

import com.example.greeting.v1.GreetRequest
import com.example.greeting.v1.GreetResponse
import com.example.server.GreetingServiceImpl
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GreetingServiceTest {
    @Test
    fun appHasAGreeting() {
        val greetingService = GreetingServiceImpl()

        runBlocking {
            val response =
                greetingService
                    .greet(
                        GreetRequest
                            .newBuilder()
                            .setName("Test")
                            .build(),
                    )

            assertEquals(response.message, "Greetings, Test!")
        }
    }
}
