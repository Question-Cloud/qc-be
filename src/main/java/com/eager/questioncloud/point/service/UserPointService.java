package com.eager.questioncloud.point.service;

import com.eager.questioncloud.point.implement.UserPointProcessor;
import com.eager.questioncloud.point.implement.UserPointReader;
import com.eager.questioncloud.point.vo.ChargePointType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointService {
    private final UserPointReader userPointReader;
    private final UserPointProcessor userPointProcessor;

    public int getUserPoint(Long userId) {
        return userPointReader.getUserPoint(userId);
    }

    public void chargePoint(Long userId, ChargePointType chargePointType, String paymentId) {
        userPointProcessor.chargePoint(userId, chargePointType, paymentId);
    }
}
