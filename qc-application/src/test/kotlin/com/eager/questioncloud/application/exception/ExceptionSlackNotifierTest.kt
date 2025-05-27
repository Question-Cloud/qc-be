package com.eager.questioncloud.application.exception

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class ExceptionSlackNotifierTest(
    @Autowired val slackNotifier: ExceptionSlackNotifier,
) {
    @Test
    fun sendApiException() {
        slackNotifier.sendApiException(Exception(), "non API", "non API", "non API")
    }
}