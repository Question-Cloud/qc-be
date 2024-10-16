package com.eager.questioncloud.point.model;

import com.eager.questioncloud.point.repository.UserPointPaymentEntity;
import com.eager.questioncloud.point.vo.ChargePointType;
import com.eager.questioncloud.portone.dto.PortonePayment;
import com.eager.questioncloud.portone.enums.PortonePaymentStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPointPayment {
    private String paymentId;
    private Long userId;
    private PortonePaymentStatus status;
    private ChargePointType chargePointType;
    private int amount;
    private String receiptUrl;
    private LocalDateTime createdAt;

    @Builder
    public UserPointPayment(String paymentId, Long userId, PortonePaymentStatus status, ChargePointType chargePointType, int amount,
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

    public static UserPointPayment create(Long userId, ChargePointType chargePointType, PortonePayment portonePayment) {
        return UserPointPayment.builder()
            .paymentId(portonePayment.getId())
            .userId(userId)
            .status(portonePayment.getStatus())
            .chargePointType(chargePointType)
            .amount(portonePayment.getAmount().getTotal())
            .receiptUrl(portonePayment.getReceiptUrl())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public UserPointPaymentEntity toEntity() {
        return UserPointPaymentEntity.builder()
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
