package com.eager.questioncloud.application.business.payment.point.implement;

import com.eager.questioncloud.application.business.payment.point.event.ChargePointPaymentEvent;
import com.eager.questioncloud.application.message.FailChargePointPaymentMessage;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointPaymentPostProcessor {
    private final UserPointManager userPointManager;
    private final MessageSender messageSender;

    @EventListener
    public void chargeUserPoint(ChargePointPaymentEvent event) {
        try {
            userPointManager.chargePoint(event.getUserId(), event.getChargePointType().getAmount());
        } catch (Exception e) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, FailChargePointPaymentMessage.create(event.getPaymentId()));
            throw new CoreException(Error.PAYMENT_ERROR);
        }
    }
}
