package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.implement.QuestionPaymentHistoryGenerator;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionOrderRepository;
import com.eager.questioncloud.core.domain.payment.infrastructure.repository.QuestionPaymentRepository;
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
    private final QuestionPaymentHistoryGenerator questionPaymentHistoryGenerator;

    @Transactional
    public QuestionPayment processQuestionPayment(QuestionPayment questionPayment) {
        questionPaymentCouponProcessor.applyCoupon(questionPayment);
        userPointManager.usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        questionOrderRepository.save(questionPayment.getOrder());

        QuestionPayment savedQuestionPayment = questionPaymentRepository.save(questionPayment);
        questionPaymentHistoryGenerator.saveQuestionPaymentHistory(savedQuestionPayment);

        return savedQuestionPayment;
    }
}
