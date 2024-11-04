package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentProcessor {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    @Transactional
    public ChargePointPayment payment(PortonePayment portonePayment) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(portonePayment.getId());
        chargePointPayment.paid(portonePayment);
        chargePointPaymentRepository.save(chargePointPayment);
        return chargePointPayment;
    }

    public ChargePointPayment createOrder(ChargePointPayment chargePointPayment) {
        return chargePointPaymentRepository.save(chargePointPayment);
    }

    public Boolean isCompletePayment(Long userId, String paymentId) {
        return chargePointPaymentRepository.isCompletedPayment(userId, paymentId);
    }
}
