package com.eager.questioncloud.core.domain.user.point.repository;

import com.eager.questioncloud.core.domain.user.point.model.ChargePointOrder;

public interface ChargePointOrderRepository {
    ChargePointOrder append(ChargePointOrder chargePointOrder);

    ChargePointOrder findByPaymentId(String paymentId);

    ChargePointOrder save(ChargePointOrder chargePointOrder);
}
