package com.eager.questioncloud.event.implement

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class EventProcessorTest(
    @Autowired private val eventProcessor: TestEventProcessor
) {
    @Test
    fun repulish() {
        runBlocking {
            eventProcessor.republishScheduled()
        }
    }
}