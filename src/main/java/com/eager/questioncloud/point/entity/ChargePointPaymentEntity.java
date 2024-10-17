package com.eager.questioncloud.point.entity;

import com.eager.questioncloud.point.model.ChargePointPayment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "charge_point_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargePointPaymentEntity {
    @Id
    private String paymentId;

    @Column
    private Long userId;

    @Column
    private String receiptUrl;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public ChargePointPaymentEntity(String paymentId, Long userId, String receiptUrl, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.receiptUrl = receiptUrl;
        this.createdAt = createdAt;
    }

    public ChargePointPayment toModel() {
        return ChargePointPayment.builder()
            .paymentId(paymentId)
            .userId(userId)
            .receiptUrl(receiptUrl)
            .createdAt(createdAt)
            .build();
    }
}
