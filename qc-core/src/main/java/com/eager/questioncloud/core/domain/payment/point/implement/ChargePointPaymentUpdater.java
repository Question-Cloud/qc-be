package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentUpdater {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public void save(ChargePointPayment chargePointPayment) {
        chargePointPaymentRepository.save(chargePointPayment);
    }
}
