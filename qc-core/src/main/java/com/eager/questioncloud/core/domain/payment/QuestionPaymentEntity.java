package com.eager.questioncloud.core.domain.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paymentId;

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
    public QuestionPaymentEntity(Long id, String paymentId, Long userId, Long userCouponId, int amount, QuestionPaymentStatus status,
        LocalDateTime createdAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public QuestionPayment toModel() {
        return QuestionPayment.builder()
            .id(id)
            .paymentId(paymentId)
            .userId(userId)
            .userCouponId(userCouponId)
            .amount(amount)
            .status(status)
            .createdAt(createdAt)
            .build();
    }

    public static QuestionPaymentEntity from(QuestionPayment questionPayment) {
        return QuestionPaymentEntity.builder()
            .id(questionPayment.getId())
            .paymentId(questionPayment.getPaymentId())
            .userId(questionPayment.getUserId())
            .userCouponId(questionPayment.getUserCouponId())
            .amount(questionPayment.getAmount())
            .status(questionPayment.getStatus())
            .createdAt(questionPayment.getCreatedAt())
            .build();
    }
}
