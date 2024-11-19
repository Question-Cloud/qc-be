package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.domain.payment.QuestionPayment;
import com.eager.questioncloud.lock.LockKeyGenerator;
import com.eager.questioncloud.lock.LockManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor questionPaymentProcessor;
    private final QuestionPaymentGenerator questionPaymentGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final LockManager lockManager;

    //TODO Event 처리
    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateQuestionPaymentKey(userId),
            () -> {
                QuestionPayment questionPayment = questionPaymentGenerator.generateQuestionPayment(userId, questionIds, userCouponId);
                questionPaymentProcessor.processQuestionPayment(questionPayment);
//                applicationEventPublisher.publishEvent(CompletedQuestionPaymentEvent.create(questionPayment, questionIds));
            }
        );
    }
}
