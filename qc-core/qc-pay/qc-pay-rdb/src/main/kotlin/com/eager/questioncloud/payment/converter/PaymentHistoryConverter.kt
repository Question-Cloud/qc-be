package com.eager.questioncloud.payment.converter

import com.eager.questioncloud.payment.domain.QuestionPaymentHistory
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import org.springframework.stereotype.Component

@Component
class PaymentHistoryConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<QuestionPaymentHistory, String> {
    override fun convertToDatabaseColumn(attribute: QuestionPaymentHistory): String {
        return objectMapper.writeValueAsString(attribute)
    }
    
    override fun convertToEntityAttribute(dbData: String): QuestionPaymentHistory {
        return objectMapper.readValue(dbData, QuestionPaymentHistory::class.java)
    }
}