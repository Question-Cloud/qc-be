package com.eager.questioncloud.core.domain.payment.infrastructure.entity;

import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "question_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPaymentEntity {
    @Id
    private String orderId;

    @Column
    private Long userId;

    @Column
    private Long userCouponId;

    @Column
    private int amount;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionPaymentStatus status;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionPaymentEntity(String orderId, Long userId, Long userCouponId, int amount, QuestionPaymentStatus status,
        LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuestionPaymentEntity from(QuestionPayment questionPayment) {
        return QuestionPaymentEntity.builder()
            .orderId(questionPayment.getOrder().getOrderId())
            .userId(questionPayment.getUserId())
            .userCouponId(questionPayment.getQuestionPaymentCoupon() != null ? questionPayment.getQuestionPaymentCoupon().getUserCouponId() : null)
            .amount(questionPayment.getAmount())
            .status(questionPayment.getStatus())
            .createdAt(questionPayment.getCreatedAt())
            .build();
    }
}
