package com.eager.questioncloud.point.model;

import com.eager.questioncloud.point.entity.ChargePointHistoryEntity;
import com.eager.questioncloud.point.vo.ChargePointType;
import com.eager.questioncloud.portone.dto.PortonePayment;
import com.eager.questioncloud.portone.enums.PortonePaymentStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointHistory {
    private String paymentId;
    private Long userId;
    private PortonePaymentStatus status;
    private ChargePointType chargePointType;
    private int amount;
    private String receiptUrl;
    private LocalDateTime createdAt;

    @Builder
    public ChargePointHistory(String paymentId, Long userId, PortonePaymentStatus status, ChargePointType chargePointType, int amount,
        String receiptUrl,
        LocalDateTime createdAt) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.status = status;
        this.chargePointType = chargePointType;
        this.amount = amount;
        this.receiptUrl = receiptUrl;
        this.createdAt = createdAt;
    }

    public static ChargePointHistory create(Long userId, ChargePointType chargePointType, PortonePayment portonePayment) {
        return ChargePointHistory.builder()
            .paymentId(portonePayment.getId())
            .userId(userId)
            .status(portonePayment.getStatus())
            .chargePointType(chargePointType)
            .amount(portonePayment.getAmount().getTotal())
            .receiptUrl(portonePayment.getReceiptUrl())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public ChargePointHistoryEntity toEntity() {
        return ChargePointHistoryEntity.builder()
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
