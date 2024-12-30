package com.eager.questioncloud.application.api.payment.point.implement;


import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentFailHandler {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgAPI;

    @RabbitListener(queues = "fail-charge-point")
    public void failHandler(FailChargePointPaymentEvent message) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(message.getPaymentId());
        chargePointPayment.fail();
        chargePointPaymentRepository.save(chargePointPayment);

        pgAPI.cancel(chargePointPayment.getPaymentId());
    }
}
