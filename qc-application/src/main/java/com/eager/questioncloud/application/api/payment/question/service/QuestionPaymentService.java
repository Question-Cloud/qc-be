package com.eager.questioncloud.application.api.payment.question.service;

import com.eager.questioncloud.application.api.payment.question.implement.QuestionOrderGenerator;
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentHistoryGenerator;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor questionPaymentProcessor;
    private final QuestionOrderGenerator questionOrderGenerator;
    private final QuestionPaymentHistoryGenerator questionPaymentHistoryGenerator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        QuestionOrder order = questionOrderGenerator.generateQuestionPaymentOrder(userId, questionIds);
        QuestionPayment paymentResult = questionPaymentProcessor.processQuestionPayment(QuestionPayment.create(userId, userCouponId, order));
        questionPaymentHistoryGenerator.saveQuestionPaymentHistory(paymentResult, questionIds);
        applicationEventPublisher.publishEvent(CompletedQuestionPaymentEvent.create(paymentResult, questionIds));
    }
}
