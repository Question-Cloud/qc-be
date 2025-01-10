package com.eager.questioncloud.application.api.payment.point.implement;


import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.ChargePointPaymentRepository;
import com.eager.questioncloud.core.domain.point.model.ChargePointPayment;
import com.eager.questioncloud.pg.implement.PGPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FailChargePointPaymentMessageListener {
    private final ChargePointPaymentRepository chargePointPaymentRepository;
    private final PGPaymentProcessor pgPaymentProcessor;

    @RabbitListener(id = "fail-charge-point", queues = "fail-charge-point")
    public void failHandler(FailChargePointPaymentMessage message) {
        ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(message.getPaymentId());
        chargePointPayment.fail();
        chargePointPaymentRepository.save(chargePointPayment);

        pgPaymentProcessor.cancel(chargePointPayment.getPaymentId());
    }
}
