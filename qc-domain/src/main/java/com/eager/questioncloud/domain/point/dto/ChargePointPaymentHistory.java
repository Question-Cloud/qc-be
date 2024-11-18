package com.eager.questioncloud.domain.point.dto;

import com.eager.questioncloud.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.domain.point.vo.ChargePointType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChargePointPaymentHistory {
    private final Long id;
    private final String paymentId;
    private String receiptUrl;
    private final ChargePointType chargePointType;
    private LocalDateTime paidAt;

    @Builder
    private ChargePointPaymentHistory(Long id, String paymentId, String receiptUrl, ChargePointType chargePointType, LocalDateTime paidAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.receiptUrl = receiptUrl;
        this.chargePointType = chargePointType;
        this.paidAt = paidAt;
    }

    public static List<ChargePointPaymentHistory> from(List<ChargePointPayment> chargePointPayments) {
        return chargePointPayments.stream()
            .map(ChargePointPaymentHistory::from)
            .collect(Collectors.toList());
    }

    public static ChargePointPaymentHistory from(ChargePointPayment chargePointPayment) {
        return ChargePointPaymentHistory.builder()
            .id(chargePointPayment.getId())
            .paymentId(chargePointPayment.getPaymentId())
            .receiptUrl(chargePointPayment.getReceiptUrl())
            .chargePointType(chargePointPayment.getChargePointType())
            .paidAt(chargePointPayment.getPaidAt())
            .build();
    }
}
