package com.eager.questioncloud.application.api.payment.point.implement;


import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
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
    private final MessageSender messageSender;

    @RabbitListener(id = "fail-charge-point", queues = "fail-charge-point")
    public void failHandler(FailChargePointPaymentMessage message) {
        try {
            ChargePointPayment chargePointPayment = chargePointPaymentRepository.findByPaymentId(message.getPaymentId());
            chargePointPayment.fail();
            chargePointPaymentRepository.save(chargePointPayment);

            pgPaymentProcessor.cancel(chargePointPayment.getPaymentId());
        } catch (Exception e) {
            message.increaseFailCount();
            messageSender.sendDelayMessage(MessageType.FAIL_CHARGE_POINT, message, message.getFailCount());
        }
    }
}
