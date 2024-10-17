package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.point.vo.ChargePointType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointProcessor {
    private final UserPointReader userPointReader;
    private final UserPointUpdater userPointUpdater;

    @Transactional
    public void chargePoint(Long userId, ChargePointType chargePointType) {
        int userPoint = userPointReader.getUserPoint(userId);
        userPointUpdater.updateUserPoint(userId, userPoint + chargePointType.getAmount());
    }

    public void usePoint(Long userId, int amount) {
        int userPoint = userPointReader.getUserPoint(userId);
        if (userPoint < amount) {
            throw new CustomException(Error.NOT_ENOUGH_POINT);
        }
        userPointUpdater.updateUserPoint(userId, userPoint - amount);
    }
}
