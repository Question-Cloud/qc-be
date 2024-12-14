package com.eager.questioncloud.core.domain.point.infrastructure;

import com.eager.questioncloud.core.common.PagingInformation;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import java.util.List;

public interface ChargePointPaymentRepository {
    ChargePointPayment save(ChargePointPayment chargePointPayment);

    Boolean isCompletedPayment(Long userId, String paymentId);

    ChargePointPayment findByPaymentId(String paymentId);

    Boolean existsByPaymentId(String paymentId);

    List<ChargePointPayment> getChargePointPayments(Long userId, PagingInformation pagingInformation);

    int countByUserId(Long userId);
}
