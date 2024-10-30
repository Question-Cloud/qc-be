package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.repository.ChargePointOrderRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointOrderReader {
    private final ChargePointOrderRepository chargePointOrderRepository;

    public ChargePointOrder findByPaymentId(String paymentId) {
        return chargePointOrderRepository.findByPaymentId(paymentId);
    }
}
