package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentOrderRepository;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;
    private final UserPointManager userPointManager;
    private final UserCouponProcessor userCouponProcessor;

    @Transactional
    public QuestionPayment questionPayment(QuestionPayment questionPayment) {
        applyCoupon(questionPayment);
        userPointManager.usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        return savePaymentInformation(questionPayment);
    }

    private void applyCoupon(QuestionPayment questionPayment) {
        if (questionPayment.isUsingCoupon()) {
            Coupon coupon = userCouponProcessor.useCoupon(questionPayment.getUserId(), questionPayment.getUserCouponId());
            questionPayment.useCoupon(coupon);
        }
    }

    private QuestionPayment savePaymentInformation(QuestionPayment questionPayment) {
        questionPaymentOrderRepository.saveAll(questionPayment.getOrders());
        return questionPaymentRepository.save(questionPayment);
    }
}
