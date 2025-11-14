package com.eager.questioncloud.config

import com.eager.questioncloud.logging.api.ApiTransactionContextHolder
import io.sentry.SentryOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SentryConfig {
    @Bean
    fun beforeSendCallback(): SentryOptions.BeforeSendCallback {
        return SentryOptions.BeforeSendCallback { event, hint ->
            if (ApiTransactionContextHolder.isActive()) {
                event.setExtra("transactionId", ApiTransactionContextHolder.get().transactionId)
            }
            event
        }
    }
}