package com.eager.questioncloud.core.domain.payment.point.model;

import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointPaymentStatus;
import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointType;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.InvalidPaymentException;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointPayment {
    private final String paymentId;
    private final Long userId;
    private String receiptUrl;
    private final ChargePointType chargePointType;
    private ChargePointPaymentStatus chargePointPaymentStatus;
    private final LocalDateTime createdAt;
    private LocalDateTime paidAt;

    @Builder
    public ChargePointPayment(String paymentId, Long userId, String receiptUrl, ChargePointType chargePointType,
        ChargePointPaymentStatus chargePointPaymentStatus, LocalDateTime createdAt, LocalDateTime paidAt) {
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

    public void approve(PortonePayment payment) {
        validatePayment(payment);
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAID;
        this.receiptUrl = payment.getReceiptUrl();
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.Fail;
    }

    private void validatePayment(PortonePayment payment) {
        validateStatus();
        validateAmount(payment);
    }

    private void validateAmount(PortonePayment payment) {
        if (chargePointType.getAmount() != payment.getAmount().getTotal()) {
            throw new InvalidPaymentException(payment);
        }
    }

    private void validateStatus() {
        if (!chargePointPaymentStatus.equals(ChargePointPaymentStatus.ORDERED)) {
            throw new CustomException(Error.ALREADY_PROCESSED_PAYMENT);
        }
    }
}
