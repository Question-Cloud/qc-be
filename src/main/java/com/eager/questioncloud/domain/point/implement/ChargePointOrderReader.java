package com.eager.questioncloud.domain.point.implement;

import com.eager.questioncloud.domain.point.model.ChargePointOrder;
import com.eager.questioncloud.domain.point.repository.ChargePointOrderRepository;
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
