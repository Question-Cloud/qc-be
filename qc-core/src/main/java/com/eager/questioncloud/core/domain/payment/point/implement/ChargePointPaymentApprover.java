package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentApprover {
    private final ChargePointPaymentRepository chargePointPaymentRepository;

    public ChargePointPayment approve(ChargePointPayment chargePointPayment, PortonePayment portonePayment) {
        chargePointPayment.paid(portonePayment);
        chargePointPaymentRepository.save(chargePointPayment);
        return chargePointPayment;
    }
}
