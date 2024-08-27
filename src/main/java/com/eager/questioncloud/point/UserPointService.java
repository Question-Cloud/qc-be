package com.eager.questioncloud.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;
    private final UserPointCharger userPointCharger;

    public int getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }

    public void chargePoint(Long userId, ChargePointType chargePointType, String paymentId) {
        userPointCharger.chargePoint(userId, chargePointType, paymentId);
    }
}
