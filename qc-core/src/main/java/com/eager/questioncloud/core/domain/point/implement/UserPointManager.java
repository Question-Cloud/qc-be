package com.eager.questioncloud.core.domain.point.implement;

import com.eager.questioncloud.core.domain.point.event.CompletedChargePointPaymentEvent;
import com.eager.questioncloud.core.domain.point.infrastructure.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import com.eager.questioncloud.core.exception.FailChargePointException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPointManager {
    private final UserPointRepository userPointRepository;

    public void init(Long userId) {
        UserPoint userPoint = new UserPoint(userId, 0);
        userPointRepository.save(userPoint);
    }

    @EventListener
    public void chargePoint(CompletedChargePointPaymentEvent completedChargePointPaymentEvent) {
        try {
            userPointRepository.chargePoint(
                completedChargePointPaymentEvent.getUserId(),
                completedChargePointPaymentEvent.getChargePointType().getAmount()
            );
        } catch (Exception e) {
            throw new FailChargePointException(completedChargePointPaymentEvent.getPaymentId());
        }
    }

    public void usePoint(Long userId, int amount) {
        if (!userPointRepository.usePoint(userId, amount)) {
            throw new CoreException(Error.NOT_ENOUGH_POINT);
        }
    }
}
