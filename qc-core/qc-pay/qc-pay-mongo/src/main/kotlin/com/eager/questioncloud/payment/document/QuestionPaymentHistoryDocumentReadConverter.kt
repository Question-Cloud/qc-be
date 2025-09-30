package com.eager.questioncloud.payment.document

import com.eager.questioncloud.payment.domain.QuestionPaymentHistoryOrder
import com.eager.questioncloud.payment.domain.SimpleDiscountHistory
import com.eager.questioncloud.payment.enums.CouponType
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
            originalAmount = (source.getInteger("originalAmount")),
            realAmount = (source.getInteger("realAmount")),
            createdAt = (convertDate(source.get("createdAt", Date::class.java)))
        )
    }
    
    private fun convertOrders(orders: List<Document>): List<QuestionPaymentHistoryOrder> {
        return orders.stream()
            .map { orderDocument: Document ->
                QuestionPaymentHistoryOrder(
                    orderDocument.getLong("orderItemId"),
                    orderDocument.getLong("questionId"),
                    orderDocument.getInteger("originalPrice"),
                    orderDocument.getInteger("realPrice"),
                    orderDocument.getString("promotionName"),
                    orderDocument.getInteger("promotionDiscountAmount"),
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
    
    private fun convertDiscountInformation(discountInformation: List<Document>): List<SimpleDiscountHistory> {
        return discountInformation.map {
            SimpleDiscountHistory(
                it.getString("title"),
                CouponType.valueOf(it.getString("couponType")),
                it.getLong("orderItemId"),
                it.getInteger("discountAmount"),
            )
        }
    }
    
    private fun convertDate(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}