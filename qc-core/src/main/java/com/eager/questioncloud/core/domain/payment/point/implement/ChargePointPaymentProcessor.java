package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentProcessor {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment approve(PortonePayment portonePayment) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(portonePayment.getId());
        chargePointPayment.paid(portonePayment);
        chargePointPaymentRepository.save(chargePointPayment);
        return chargePointPayment;
    }

    public ChargePointPayment createOrder(ChargePointPayment chargePointPayment) {
        if (chargePointPaymentRepository.existsByPaymentId(chargePointPayment.getPaymentId())) {
            throw new CustomException(Error.ALREADY_ORDERED);
        }
        return chargePointPaymentRepository.save(chargePointPayment);
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }
}
