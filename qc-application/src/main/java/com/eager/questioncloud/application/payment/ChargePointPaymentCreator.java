package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.domain.point.ChargePointPayment;
import com.eager.questioncloud.domain.point.ChargePointPaymentRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentCreator {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment createOrder(ChargePointPayment chargePointPayment) {
        if (chargePointPaymentRepository.existsByPaymentId(chargePointPayment.getPaymentId())) {
            throw new CustomException(Error.ALREADY_ORDERED);
        }
        return chargePointPaymentRepository.save(chargePointPayment);
    }
}
