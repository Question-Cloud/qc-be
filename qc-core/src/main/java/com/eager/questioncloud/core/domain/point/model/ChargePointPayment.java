package com.eager.questioncloud.core.domain.point.model;

import com.eager.questioncloud.core.domain.point.enums.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.point.enums.ChargePointType;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.InvalidPaymentException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointPayment {
    private final Long id;
    private final String paymentId;
    private final Long userId;
    private String receiptUrl;
    private final ChargePointType chargePointType;
    private ChargePointPaymentStatus chargePointPaymentStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime paidAt;

    @Builder
    public ChargePointPayment(Long id, String paymentId, Long userId, String receiptUrl, ChargePointType chargePointType,
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


    public static ChargePointPayment order(String paymentId, Long userId, ChargePointType chargePointType) {
        return ChargePointPayment.builder()
            .paymentId(paymentId)
            .userId(userId)
            .chargePointType(chargePointType)
            .chargePointPaymentStatus(ChargePointPaymentStatus.ORDERED)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void approve(String receiptUrl) {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAID;
        this.receiptUrl = receiptUrl;
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.Fail;
    }

    public void validatePayment(int paidAmount) {
        validateStatus();
        validateAmount(paidAmount);
    }

    private void validateAmount(int paidAmount) {
        if (chargePointType.getAmount() != paidAmount) {
            throw new InvalidPaymentException();
        }
    }

    private void validateStatus() {
        if (!chargePointPaymentStatus.equals(ChargePointPaymentStatus.ORDERED)) {
            throw new CoreException(Error.ALREADY_PROCESSED_PAYMENT);
        }
    }
}
