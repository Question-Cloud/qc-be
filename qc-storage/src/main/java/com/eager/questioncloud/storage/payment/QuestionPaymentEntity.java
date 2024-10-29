package com.eager.questioncloud.storage.payment;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
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
    private Long userCouponId;

    @Column
    private int amount;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public QuestionPaymentEntity(Long id, Long userId, Long userCouponId, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public QuestionPayment toModel() {
        return QuestionPayment.builder()
            .id(id)
            .userId(userId)
            .userCouponId(userCouponId)
            .amount(amount)
            .createdAt(createdAt)
            .build();
    }

    public static QuestionPaymentEntity from(QuestionPayment questionPayment) {
        return QuestionPaymentEntity.builder()
            .id(questionPayment.getId())
            .userId(questionPayment.getUserId())
            .userCouponId(questionPayment.getUserCouponId())
            .amount(questionPayment.getAmount())
            .createdAt(questionPayment.getCreatedAt())
            .build();
    }
}
