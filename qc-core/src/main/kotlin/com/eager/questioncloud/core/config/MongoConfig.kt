package com.eager.questioncloud.core.config

import com.eager.questioncloud.core.domain.payment.infrastructure.document.QuestionPaymentHistoryDocumentReadConverter
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    override fun getDatabaseName(): String {
        return "questioncloud"
    }

    override fun configureConverters(adapter: MongoConverterConfigurationAdapter) {
        adapter.registerConverter(QuestionPaymentHistoryDocumentReadConverter())
    }
}
