package com.eager.questioncloud.storage.point;

import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.point.vo.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.vo.ChargePointType;
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
@Table(name = "charge_point_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargePointPaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
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
    public ChargePointPaymentEntity(Long id, String paymentId, Long userId, String receiptUrl, ChargePointType chargePointType,
        ChargePointPaymentStatus chargePointPaymentStatus, LocalDateTime createdAt, LocalDateTime paidAt) {
        this.id = id;
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
            .id(id)
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
            .id(chargePointPayment.getId())
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
