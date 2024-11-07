package com.eager.questioncloud.core.domain.payment.question.service;

import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionAppender;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.payment.question.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor paymentProcessor;
    private final UserQuestionAppender userQuestionAppender;
    private final QuestionReader questionReader;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        List<Question> questions = questionReader.getQuestions(questionIds);
        paymentProcessor.questionPayment(QuestionPayment.create(userId, userCouponId, questions));
        userQuestionAppender.appendUserQuestions(userId, questionIds);
    }
}
