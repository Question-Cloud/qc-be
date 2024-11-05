package com.eager.questioncloud.core.domain.payment.point.event;

import com.eager.questioncloud.core.domain.payment.point.implement.ChargePointPaymentExceptionHandler;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointEvent;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointEventListener {
    private final UserPointManager userPointManager;
    private final ChargePointPaymentExceptionHandler chargePointPaymentExceptionHandler;

    @EventListener
    public void chargePointEvent(ChargePointEvent chargePointEvent) {
        try {
            userPointManager.chargePoint(chargePointEvent.getUserId(), chargePointEvent.getChargePointType());
        } catch (Exception e) {
            chargePointPaymentExceptionHandler.failHandler(chargePointEvent.getPaymentId());
            throw new CustomException(Error.PAYMENT_ERROR);
        }
    }
}
