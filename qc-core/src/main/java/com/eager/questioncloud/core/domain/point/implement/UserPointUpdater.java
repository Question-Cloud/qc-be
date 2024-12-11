package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.event.ChargePointEvent;
import com.eager.questioncloud.core.domain.point.infrastructure.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.exception.FailChargePointException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPointUpdater {
    private final UserPointRepository userPointRepository;

    @EventListener
    public void chargePoint(ChargePointEvent chargePointEvent) {
        try {
            UserPoint userPoint = userPointRepository.getUserPoint(chargePointEvent.getUserId());
            userPoint.charge(chargePointEvent.getChargePointType().getAmount());
            userPointRepository.save(userPoint);
        } catch (Exception e) {
            throw new FailChargePointException(chargePointEvent.getPaymentId());
        }
    }
}
