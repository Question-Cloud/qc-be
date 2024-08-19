package com.eager.questioncloud.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Long userId;

    @Column
    private Long couponId;

    @Column
    private int amount;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionPaymentEntity(Long id, Long userId, Long couponId, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public QuestionPayment toModel() {
        return QuestionPayment.builder()
            .id(id)
            .userId(userId)
            .couponId(couponId)
            .amount(amount)
            .createdAt(createdAt)
            .build();
    }
}
