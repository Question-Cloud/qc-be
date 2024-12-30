package com.eager.questioncloud.application.api.payment.point.service;

import com.eager.questioncloud.application.api.payment.point.event.ChargePointPaymentEvent;
import com.eager.questioncloud.application.api.payment.point.event.FailChargePointPaymentEvent;
import com.eager.questioncloud.application.message.MessageSender;
import com.eager.questioncloud.application.message.MessageType;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChargePointService {
    private final UserPointManager userPointManager;
    private final MessageSender messageSender;

    @EventListener
    public void chargeUserPoint(ChargePointPaymentEvent event) {
        try {
            userPointManager.chargePoint(event.getUserId(), event.getChargePointType().getAmount());
        } catch (Exception e) {
            messageSender.sendMessage(MessageType.FAIL_CHARGE_POINT, new FailChargePointPaymentEvent(event.getPaymentId()));
        }
    }
}
