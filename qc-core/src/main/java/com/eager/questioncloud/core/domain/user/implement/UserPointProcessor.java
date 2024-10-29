package com.eager.questioncloud.core.domain.user.implement;

import com.eager.questioncloud.core.domain.user.vo.ChargePointType;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
