package com.eager.questioncloud.core.domain.point.model;

import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
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
}
