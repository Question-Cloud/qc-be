package com.eager.questioncloud.domain.point.repository;

import com.eager.questioncloud.domain.point.model.ChargePointOrder;

public interface ChargePointOrderRepository {
    ChargePointOrder append(ChargePointOrder chargePointOrder);

    ChargePointOrder findByPaymentId(String paymentId);

    ChargePointOrder save(ChargePointOrder chargePointOrder);
}
