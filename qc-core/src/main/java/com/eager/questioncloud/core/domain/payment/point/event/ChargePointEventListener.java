package com.eager.questioncloud.core.domain.payment.point.event;

import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.point.model.ChargePointEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargePointEventListener {
    private final UserPointManager userPointManager;

    @EventListener
    public void chargePointEvent(ChargePointEvent chargePointEvent) {
        userPointManager.chargePoint(
            chargePointEvent.getUserId(),
            chargePointEvent.getChargePointType()
        );
    }
}
