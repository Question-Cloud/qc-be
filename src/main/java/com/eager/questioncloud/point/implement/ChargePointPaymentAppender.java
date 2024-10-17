package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.model.ChargePointPayment;
import com.eager.questioncloud.point.repository.ChargePointPaymentRepository;
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
