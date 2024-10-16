package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.model.ChargePointHistory;
import com.eager.questioncloud.point.repository.ChargePointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointPaymentAppender {
    private final ChargePointHistoryRepository chargePointHistoryRepository;

    public ChargePointHistory append(ChargePointHistory chargePointHistory) {
        return chargePointHistoryRepository.save(chargePointHistory);
    }
}
