package com.eager.questioncloud.core.domain.point.repository;

import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean isCompletedPayment(Long userId, String paymentId);

    ChargePointPayment getChargePointPaymentForApprove(String paymentId);

    ChargePointPayment findByPaymentId(String paymentId);

    Boolean existsByPaymentId(String paymentId);
}
