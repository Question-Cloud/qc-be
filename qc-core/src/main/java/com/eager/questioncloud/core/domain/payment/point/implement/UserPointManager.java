package com.eager.questioncloud.core.domain.payment.point.implement;

import com.eager.questioncloud.core.domain.payment.point.model.UserPoint;
import com.eager.questioncloud.core.domain.payment.point.repository.UserPointRepository;
import com.eager.questioncloud.core.domain.payment.point.vo.ChargePointType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointManager {
    private final UserPointReader userPointReader;
    private final UserPointRepository userPointRepository;

    public void chargePoint(Long userId, ChargePointType chargePointType) {
        UserPoint userPoint = userPointReader.getUserPoint(userId);
        userPoint.charge(chargePointType.getAmount());
        userPointRepository.save(userPoint);
    }

    public void usePoint(Long userId, int amount) {
        UserPoint userPoint = userPointReader.getUserPoint(userId);
        if (userPoint.getPoint() < amount) {
            throw new CustomException(Error.NOT_ENOUGH_POINT);
        }
        userPoint.use(amount);
        userPointRepository.save(userPoint);
    }

    public void chargePoint(Long userId, int amount) {
        UserPoint userPoint = userPointReader.getUserPoint(userId);
        userPoint.charge(amount);
        userPointRepository.save(userPoint);
    }
}
