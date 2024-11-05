package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
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
