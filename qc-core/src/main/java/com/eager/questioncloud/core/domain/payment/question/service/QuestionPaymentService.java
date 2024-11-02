package com.eager.questioncloud.core.domain.payment.question.service;

import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionAppender;
import com.eager.questioncloud.core.domain.payment.question.implement.QuestionPaymentProcessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor paymentProcessor;
    private final UserQuestionAppender userQuestionAppender;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        paymentProcessor.questionPayment(userId, questionIds, userCouponId);
        userQuestionAppender.appendUserQuestions(userId, questionIds);
    }
}
