package com.eager.questioncloud.core.domain.payment.point.repository;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean existsByUserIdAndPaymentId(Long userId, String paymentId);
}
