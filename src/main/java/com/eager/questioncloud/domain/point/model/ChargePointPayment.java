package com.eager.questioncloud.domain.point.model;

import com.eager.questioncloud.domain.point.entity.ChargePointPaymentEntity;
import com.eager.questioncloud.domain.portone.dto.PortonePayment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointPayment {
    private String paymentId;
    private Long userId;
    private String receiptUrl;
    private LocalDateTime createdAt;

    @Builder
    public ChargePointPayment(String paymentId, Long userId, String receiptUrl, LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.receiptUrl = receiptUrl;
        this.createdAt = createdAt;
    }

    public static ChargePointPayment create(Long userId, PortonePayment portonePayment) {
        return ChargePointPayment.builder()
            .paymentId(portonePayment.getId())
            .userId(userId)
            .receiptUrl(portonePayment.getReceiptUrl())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public ChargePointPaymentEntity toEntity() {
        return ChargePointPaymentEntity.builder()
            .paymentId(paymentId)
            .userId(userId)
            .receiptUrl(receiptUrl)
            .createdAt(createdAt)
            .build();
    }
}
