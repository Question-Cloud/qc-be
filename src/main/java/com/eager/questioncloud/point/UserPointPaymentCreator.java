package com.eager.questioncloud.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPointPaymentCreator {
    private final UserPointPaymentRepository userPointPaymentRepository;

    public UserPointPayment append(UserPointPayment userPointPayment) {
        return userPointPaymentRepository.append(userPointPayment);
    }
}
