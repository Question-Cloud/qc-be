package com.eager.questioncloud.payment.config

import com.eager.questioncloud.payment.document.QuestionPaymentHistoryDocumentReadConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions

@Configuration
class MongoConfig {
    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(QuestionPaymentHistoryDocumentReadConverter()))
    }
}
