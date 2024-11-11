package com.eager.questioncloud.core.domain.point.event;

import com.eager.questioncloud.core.domain.point.implement.ChargePointPaymentFailHandler;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointEventListener {
    private final UserPointManager userPointManager;
    private final ChargePointPaymentFailHandler chargePointPaymentFailHandler;

    @EventListener
    public void chargePointEvent(ChargePointEvent chargePointEvent) {
        try {
            userPointManager.chargePoint(chargePointEvent.getUserId(), chargePointEvent.getChargePointType());
        } catch (Exception e) {
            chargePointPaymentFailHandler.failHandler(chargePointEvent.getPaymentId());
            throw new CustomException(Error.PAYMENT_ERROR);
        }
    }
}
