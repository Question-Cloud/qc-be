package com.eager.questioncloud.application.business.payment.question.service;

import com.eager.questioncloud.application.business.payment.question.event.QuestionPaymentEvent;
import com.eager.questioncloud.application.business.payment.question.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor questionPaymentProcessor;
    private final ApplicationEventPublisher eventPublisher;

    public void payment(Long userId, QuestionOrder order, QuestionPaymentCoupon questionPaymentCoupon) {
        QuestionPayment questionPayment = QuestionPayment.create(userId, questionPaymentCoupon, order);
        questionPaymentProcessor.payment(questionPayment);
        eventPublisher.publishEvent(new QuestionPaymentEvent(questionPayment));
    }
}
