package com.eager.questioncloud.core.domain.hub.payment.service;

import com.eager.questioncloud.core.domain.hub.payment.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.library.implement.UserQuestionLibraryAppender;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor paymentProcessor;
    private final UserQuestionLibraryAppender userQuestionLibraryAppender;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        paymentProcessor.questionPayment(userId, questionIds, userCouponId);
        userQuestionLibraryAppender.appendUserQuestions(userId, questionIds);
    }
}
