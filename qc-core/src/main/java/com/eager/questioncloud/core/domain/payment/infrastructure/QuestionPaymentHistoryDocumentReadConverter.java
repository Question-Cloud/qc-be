package com.eager.questioncloud.core.domain.payment.infrastructure;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory.QuestionPaymentHistoryOrder;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;

public class QuestionPaymentHistoryDocumentReadConverter implements Converter<Document, QuestionPaymentHistoryDocument> {
    @Override
    public QuestionPaymentHistoryDocument convert(Document source) {
        return QuestionPaymentHistoryDocument.builder()
            .paymentId(source.getLong("_id"))
            .orderId(source.getString("orderId"))
            .userId(source.getLong("userId"))
            .orders(convertOrders(source.getList("orders", Document.class)))
            .coupon(convertCoupon(source))
            .amount(source.getInteger("amount"))
            .isUsedCoupon(source.getBoolean("isUsedCoupon"))
            .status(QuestionPaymentStatus.valueOf(source.getString("status")))
            .createdAt(convertDate(source.get("createdAt", Date.class)))
            .build();
    }

    private List<QuestionPaymentHistoryOrder> convertOrders(List<Document> orders) {
        return orders.stream()
            .map(orderDocument -> new QuestionPaymentHistoryOrder(
                orderDocument.getLong("questionId"),
                orderDocument.getInteger("amount"),
                orderDocument.getString("title"),
                orderDocument.getString("thumbnail"),
                orderDocument.getString("creatorName"),
                Subject.valueOf(orderDocument.getString("subject")),
                orderDocument.getString("mainCategory"),
                orderDocument.getString("subCategory")
            ))
            .toList();
    }

    private QuestionPaymentCoupon convertCoupon(Document source) {
        if (!source.getBoolean("isUsedCoupon", false)) {
            return null;
        }

        Document coupon = source.get("coupon", Document.class);
        if (coupon == null) {
            return null;
        }

        return new QuestionPaymentCoupon(
            getEmbeddedValue(coupon, "userCouponId", Long.class),
            getEmbeddedValue(coupon, "title", String.class),
            Optional.ofNullable(getEmbeddedValue(coupon, "couponType", String.class))
                .map(CouponType::valueOf)
                .orElse(null),
            getEmbeddedValue(coupon, "value", Integer.class)
        );
    }

    private <T> T getEmbeddedValue(Document document, String key, Class<T> clazz) {
        return document.get(key, clazz);
    }

    private LocalDateTime convertDate(Date date) {
        return date != null
            ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            : null;
    }
}