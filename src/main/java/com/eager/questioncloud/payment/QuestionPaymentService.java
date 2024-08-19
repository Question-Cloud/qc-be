package com.eager.questioncloud.payment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor paymentProcessor;

    public void payment(Long userId, List<Long> questionIds, Long couponId) {
        paymentProcessor.questionPayment(userId, questionIds, couponId);
    }
}
