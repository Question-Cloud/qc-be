package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.model.ChargePointOrder;

public interface ChargePointOrderRepository {
    ChargePointOrder append(ChargePointOrder chargePointOrder);
}
