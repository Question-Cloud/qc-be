package com.eager.questioncloud.core.domain.payment.point.repository;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean isCompletedPayment(Long userId, String paymentId);

    ChargePointPayment getChargePointPaymentForApprove(String paymentId);

    Boolean existsByPaymentId(String paymentId);
}
