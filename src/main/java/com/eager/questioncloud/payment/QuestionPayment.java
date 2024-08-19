package com.eager.questioncloud.payment;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private Long userId;
    private int amount;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, Long userId, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public QuestionPaymentEntity toEntity() {
        return QuestionPaymentEntity.builder()
            .id(id)
            .userId(userId)
            .amount(amount)
            .createdAt(createdAt)
            .build();
    }
}
