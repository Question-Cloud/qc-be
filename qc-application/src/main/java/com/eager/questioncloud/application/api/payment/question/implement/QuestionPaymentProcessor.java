package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionOrderRepository;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final QuestionOrderRepository questionOrderRepository;
    private final QuestionPaymentCouponProcessor questionPaymentCouponProcessor;
    private final UserPointManager userPointManager;

    @Transactional
    public QuestionPayment processQuestionPayment(QuestionPayment questionPayment) {
        questionPaymentCouponProcessor.applyCoupon(questionPayment);
        userPointManager.usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        questionOrderRepository.save(questionPayment.getOrder());
        return questionPaymentRepository.save(questionPayment);
    }
}
