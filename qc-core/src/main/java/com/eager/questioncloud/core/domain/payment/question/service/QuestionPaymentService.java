package com.eager.questioncloud.core.domain.payment.question.service;

import com.eager.questioncloud.core.domain.feed.library.event.AppendUserQuestionEvent;
import com.eager.questioncloud.core.domain.feed.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.domain.hub.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.payment.question.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor paymentProcessor;
    private final QuestionReader questionReader;
    private final UserQuestionReader userQuestionReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        checkAlreadyOwn(userId, questionIds);
        List<Question> questions = questionReader.getQuestions(questionIds);
        paymentProcessor.questionPayment(QuestionPayment.create(userId, userCouponId, questions));
        applicationEventPublisher.publishEvent(AppendUserQuestionEvent.create(userId, questionIds));
    }

    private void checkAlreadyOwn(Long userId, List<Long> questionIds) {
        if (userQuestionReader.isOwned(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
    }
}
