package com.eager.questioncloud.domain.point;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.exception.InvalidPaymentException;
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

    public void approve(PGPayment pgPayment) {
        validatePayment(pgPayment);
        this.chargePointPaymentStatus = ChargePointPaymentStatus.PAID;
        this.receiptUrl = pgPayment.getReceiptUrl();
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        this.chargePointPaymentStatus = ChargePointPaymentStatus.Fail;
    }

    private void validatePayment(PGPayment pgPayment) {
        validateStatus();
        validateAmount(pgPayment);
    }

    private void validateAmount(PGPayment pgPayment) {
        if (chargePointType.getAmount() != pgPayment.getAmount()) {
            throw new InvalidPaymentException(pgPayment);
        }
    }

    private void validateStatus() {
        if (!chargePointPaymentStatus.equals(ChargePointPaymentStatus.ORDERED)) {
            throw new CustomException(Error.ALREADY_PROCESSED_PAYMENT);
        }
    }
}
