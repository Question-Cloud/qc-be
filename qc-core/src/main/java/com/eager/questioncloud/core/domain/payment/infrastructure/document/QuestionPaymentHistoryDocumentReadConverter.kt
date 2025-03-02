package com.eager.questioncloud.core.domain.payment.infrastructure.document

import com.eager.questioncloud.core.domain.coupon.enums.CouponType
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistoryOrder
import com.eager.questioncloud.core.domain.question.enums.Subject
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
            coupon = (convertCoupon(source)),
            amount = (source.getInteger("amount")),
            isUsedCoupon = (source.getBoolean("isUsedCoupon")),
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
                    Subject.valueOf(orderDocument.getString("subject")),
                    orderDocument.getString("mainCategory"),
                    orderDocument.getString("subCategory")
                )
            }
            .toList()
    }

    private fun convertCoupon(source: Document): QuestionPaymentCoupon? {
        if (!source.getBoolean("isUsedCoupon", false)) {
            return null
        }

        val coupon = source.get("coupon", Document::class.java)

        return QuestionPaymentCoupon(
            coupon.getLong("userCouponId"),
            coupon.getString("title"),
            CouponType.valueOf(coupon.getString("couponType")),
            coupon.getInteger("value")
        )
    }

    private fun convertDate(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}