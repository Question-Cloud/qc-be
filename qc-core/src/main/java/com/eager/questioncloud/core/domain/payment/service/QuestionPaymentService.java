package com.eager.questioncloud.core.domain.payment.service;

import com.eager.questioncloud.core.common.LockKeyGenerator;
import com.eager.questioncloud.core.common.LockManager;
import com.eager.questioncloud.core.domain.cart.event.ClearCartItemEvent;
import com.eager.questioncloud.core.domain.library.event.AppendUserQuestionAfterPaymentEvent;
import com.eager.questioncloud.core.domain.library.implement.UserQuestionReader;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.question.implement.QuestionReader;
import com.eager.questioncloud.core.domain.question.model.Question;
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
    private final LockManager lockManager;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        lockManager.executeWithLock(
            LockKeyGenerator.generateQuestionPaymentKey(userId),
            () -> {
                checkAlreadyOwn(userId, questionIds);
                List<Question> questions = questionReader.getQuestions(questionIds);
                QuestionPayment questionPayment = paymentProcessor.questionPayment(QuestionPayment.create(userId, userCouponId, questions));
                applicationEventPublisher.publishEvent(AppendUserQuestionAfterPaymentEvent.create(userId, questionIds, questionPayment));
                applicationEventPublisher.publishEvent(ClearCartItemEvent.create(userId, questionIds));
            }
        );
    }

    private void checkAlreadyOwn(Long userId, List<Long> questionIds) {
        if (userQuestionReader.isOwned(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
    }
}
