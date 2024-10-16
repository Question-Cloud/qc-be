package com.eager.questioncloud.point.implement;

import com.eager.questioncloud.point.model.ChargePointHistory;
import com.eager.questioncloud.point.repository.UserPointPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointPaymentAppender {
    private final UserPointPaymentRepository userPointPaymentRepository;

    public ChargePointHistory append(ChargePointHistory chargePointHistory) {
        return userPointPaymentRepository.save(chargePointHistory);
    }
}
