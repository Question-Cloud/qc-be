package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.ChargePointPayment;
import com.eager.questioncloud.core.domain.payment.point.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.portone.implement.PortoneAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentFailHandler {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PortoneAPI portoneAPI;

    public void failHandler(String paymentId) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId);
        chargePointPayment.fail();
        chargePointPaymentRepository.save(chargePointPayment);

        portoneAPI.cancel(chargePointPayment.getPaymentId());
    }
}
