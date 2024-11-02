package com.eager.questioncloud.storage.point;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointType;
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
    @Enumerated(EnumType.STRING)
    private ChargePointType chargePointType;

    @Column
    @Enumerated(EnumType.STRING)
    private ChargePointPaymentStatus chargePointPaymentStatus;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime paidAt;

    @Builder
    public ChargePointPaymentEntity(String paymentId, Long userId, String receiptUrl, ChargePointType chargePointType,
        ChargePointPaymentStatus chargePointPaymentStatus, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.receiptUrl = receiptUrl;
        this.chargePointType = chargePointType;
        this.chargePointPaymentStatus = chargePointPaymentStatus;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
    }

    public ChargePointPayment toModel() {
        return ChargePointPayment.builder()
            .paymentId(paymentId)
            .userId(userId)
            .receiptUrl(receiptUrl)
            .chargePointType(chargePointType)
            .chargePointPaymentStatus(chargePointPaymentStatus)
            .createdAt(createdAt)
            .paidAt(paidAt)
            .build();
    }

    public static ChargePointPaymentEntity from(ChargePointPayment chargePointPayment) {
        return ChargePointPaymentEntity.builder()
            .paymentId(chargePointPayment.getPaymentId())
            .userId(chargePointPayment.getUserId())
            .receiptUrl(chargePointPayment.getReceiptUrl())
            .chargePointType(chargePointPayment.getChargePointType())
            .chargePointPaymentStatus(chargePointPayment.getChargePointPaymentStatus())
            .createdAt(chargePointPayment.getCreatedAt())
            .paidAt(chargePointPayment.getPaidAt())
            .build();
    }
}
