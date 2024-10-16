package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointType;
import com.eager.questioncloud.point.model.UserPointPayment;
import com.eager.questioncloud.portone.enums.PortonePaymentStatus;
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
@Table(name = "user_point_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointPaymentEntity {
    @Id
    private String paymentId;

    @Column
    private Long userId;

    @Column
    @Enumerated(EnumType.STRING)
    private PortonePaymentStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private ChargePointType chargePointType;

    @Column
    private int amount;

    @Column
    private String receiptUrl;

    @Column
    private LocalDateTime createdAt;

    @Builder
    public UserPointPaymentEntity(String paymentId, Long userId, PortonePaymentStatus status, ChargePointType chargePointType, int amount,
        String receiptUrl, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.status = status;
        this.chargePointType = chargePointType;
        this.amount = amount;
        this.receiptUrl = receiptUrl;
        this.createdAt = createdAt;
    }

    public UserPointPayment toModel() {
        return UserPointPayment.builder()
            .paymentId(paymentId)
            .userId(userId)
            .status(status)
            .chargePointType(chargePointType)
            .amount(amount)
            .receiptUrl(receiptUrl)
            .createdAt(createdAt)
            .build();
    }
}
