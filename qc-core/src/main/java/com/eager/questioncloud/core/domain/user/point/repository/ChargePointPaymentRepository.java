package com.eager.questioncloud.core.domain.user.point.repository;

import com.eager.questioncloud.core.domain.user.point.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean existsByUserIdAndPaymentId(Long userId, String paymentId);
}
