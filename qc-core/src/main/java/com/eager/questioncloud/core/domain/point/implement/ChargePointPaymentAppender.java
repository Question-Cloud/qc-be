package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentAppender {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment append(ChargePointPayment chargePointPayment) {
        return chargePointPaymentRepository.save(chargePointPayment);
    }
}
