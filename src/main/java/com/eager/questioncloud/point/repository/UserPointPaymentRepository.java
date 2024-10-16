package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointHistory;

public interface UserPointPaymentRepository {
    ChargePointHistory save(ChargePointHistory chargePointHistory);

    Boolean existsById(String paymentId);
}
