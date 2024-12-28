package com.eager.questioncloud.core.domain.payment.infrastructure.document;

import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentHistory.QuestionPaymentHistoryOrder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "question_payment_history")
@Getter
@Builder
public class QuestionPaymentHistoryDocument {
    @Id
    private Long paymentId;
    private String orderId;
    private Long userId;
    private List<QuestionPaymentHistoryOrder> orders;
    private QuestionPaymentCoupon coupon;
    private int amount;
    private Boolean isUsedCoupon;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    public QuestionPaymentHistory toModel() {
        return QuestionPaymentHistory.builder()
            .paymentId(paymentId)
            .orderId(orderId)
            .userId(userId)
            .orders(orders)
            .coupon(coupon)
            .amount(amount)
            .isUsedCoupon(isUsedCoupon)
            .status(status)
            .createdAt(createdAt)
            .build();
    }

    public static QuestionPaymentHistoryDocument from(QuestionPaymentHistory questionPaymentHistory) {
        return QuestionPaymentHistoryDocument
            .builder()
            .paymentId(questionPaymentHistory.getPaymentId())
            .orderId(questionPaymentHistory.getOrderId())
            .userId(questionPaymentHistory.getUserId())
            .orders(questionPaymentHistory.getOrders())
            .coupon(questionPaymentHistory.getCoupon())
            .amount(questionPaymentHistory.getAmount())
            .isUsedCoupon(questionPaymentHistory.getIsUsedCoupon())
            .status(questionPaymentHistory.getStatus())
            .createdAt(questionPaymentHistory.getCreatedAt())
            .build();
    }
}
