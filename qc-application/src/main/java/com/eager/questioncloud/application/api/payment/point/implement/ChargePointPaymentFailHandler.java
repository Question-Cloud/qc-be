package com.eager.questioncloud.application.api.payment.point.implement;


import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentFailHandler {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGAPI pgAPI;

    public void failHandler(String paymentId) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(paymentId);
        chargePointPayment.fail();
        chargePointPaymentRepository.save(chargePointPayment);

        pgAPI.cancel(chargePointPayment.getPaymentId());
    }
}
