package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.model.ChargePointOrder;
import com.eager.questioncloud.core.domain.user.repository.ChargePointOrderRepository;
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
