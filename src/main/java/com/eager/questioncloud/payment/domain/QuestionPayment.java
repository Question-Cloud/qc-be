package com.eager.questioncloud.payment.domain;

import com.eager.questioncloud.payment.entity.QuestionPaymentEntity;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private Long userId;
    private Long couponId;
    private int amount;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, Long userId, Long couponId, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, Long couponId, int amount) {
        return QuestionPayment.builder()
            .userId(userId)
            .couponId(couponId)
            .amount(amount)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public QuestionPaymentEntity toEntity() {
        return QuestionPaymentEntity.builder()
            .id(id)
            .userId(userId)
            .couponId(couponId)
            .amount(amount)
            .createdAt(createdAt)
            .build();
    }
}
