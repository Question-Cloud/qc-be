package com.eager.questioncloud.application.api.payment.question.service;

import com.eager.questioncloud.application.api.payment.question.implement.QuestionOrderGenerator;
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentCouponProcessor;
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentPostProcessor;
import com.eager.questioncloud.application.api.payment.question.implement.QuestionPaymentProcessor;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionPaymentService {
    private final QuestionPaymentProcessor questionPaymentProcessor;
    private final QuestionOrderGenerator questionOrderGenerator;
    private final QuestionPaymentCouponProcessor questionPaymentCouponProcessor;
    private final QuestionPaymentPostProcessor questionPaymentPostProcessor;

    public void payment(Long userId, List<Long> questionIds, Long userCouponId) {
        QuestionOrder order = questionOrderGenerator.generateQuestionOrder(userId, questionIds);
        QuestionPaymentCoupon questionPaymentCoupon = questionPaymentCouponProcessor.getCoupon(userCouponId, userId);
        QuestionPayment paymentResult = questionPaymentProcessor.processQuestionPayment(QuestionPayment.create(userId, questionPaymentCoupon, order));

        questionPaymentPostProcessor.postProcess(paymentResult);
    }
}
