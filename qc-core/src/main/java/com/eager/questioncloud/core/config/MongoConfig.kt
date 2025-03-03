package com.eager.questioncloud.core.config;

import com.eager.questioncloud.core.domain.payment.infrastructure.document.QuestionPaymentHistoryDocumentReadConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions.MongoConverterConfigurationAdapter;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    protected String getDatabaseName() {
        return "questioncloud";
    }

    @Override
    protected void configureConverters(MongoConverterConfigurationAdapter adapter) {
        adapter.registerConverter(new QuestionPaymentHistoryDocumentReadConverter());
    }
}
