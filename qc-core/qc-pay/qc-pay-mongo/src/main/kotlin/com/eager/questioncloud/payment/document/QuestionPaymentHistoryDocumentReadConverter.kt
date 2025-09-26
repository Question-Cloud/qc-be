package com.eager.questioncloud.payment.document

import com.eager.questioncloud.common.event.DiscountInformation
import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.enums.QuestionPaymentStatus
import org.bson.Document
import org.springframework.core.convert.converter.Converter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class QuestionPaymentHistoryDocumentReadConverter :
    Converter<Document, QuestionPaymentHistoryDocument> {
    override fun convert(source: Document): QuestionPaymentHistoryDocument {
        return QuestionPaymentHistoryDocument(
            orderId = (source.getString("_id")),
            userId = (source.getLong("userId")),
            orders = (convertOrders(source.getList("orders", Document::class.java))),
            discountInformation = (convertDiscountInformation(source.getList("discountInformation", Document::class.java))),
            amount = (source.getInteger("amount")),
            status = (QuestionPaymentStatus.valueOf(source.getString("status"))),
            createdAt = (convertDate(source.get("createdAt", Date::class.java)))
        )
    }
    
    private fun convertOrders(orders: List<Document>): List<QuestionPaymentHistoryOrder> {
        return orders.stream()
            .map { orderDocument: Document ->
                QuestionPaymentHistoryOrder(
                    orderDocument.getLong("questionId"),
                    orderDocument.getInteger("amount"),
                    orderDocument.getString("title"),
                    orderDocument.getString("thumbnail"),
                    orderDocument.getString("creatorName"),
                    orderDocument.getString("subject"),
                    orderDocument.getString("mainCategory"),
                    orderDocument.getString("subCategory")
                )
            }
            .toList()
    }
    
    private fun convertDiscountInformation(discountInformation: List<Document>): List<DiscountInformation> {
        return discountInformation.map { DiscountInformation(it.getString("title"), it.getInteger("value")) }
    }
    
    private fun convertDate(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}