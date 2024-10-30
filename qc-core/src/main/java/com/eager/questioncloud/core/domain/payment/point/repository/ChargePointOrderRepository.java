package com.eager.questioncloud.core.domain.payment.point.repository;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointOrder;

public interface ChargePointOrderRepository {
    ChargePointOrder append(ChargePointOrder chargePointOrder);

    ChargePointOrder findByPaymentId(String paymentId);

    ChargePointOrder save(ChargePointOrder chargePointOrder);
}
