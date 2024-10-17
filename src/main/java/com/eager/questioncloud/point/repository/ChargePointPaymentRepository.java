package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointPayment;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean existsById(String paymentId);
}
