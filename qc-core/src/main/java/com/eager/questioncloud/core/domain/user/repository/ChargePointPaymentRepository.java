package com.eager.questioncloud.core.domain.user.repository;

import com.eager.questioncloud.core.domain.user.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean existsByUserIdAndPaymentId(Long userId, String paymentId);
}
