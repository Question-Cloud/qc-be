package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.point.implement.UserPointManager;
import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentAppender questionPaymentAppender;
    private final QuestionPaymentOrderAppender questionPaymentOrderAppender;
    private final UserPointManager userPointManager;
    private final UserCouponProcessor userCouponProcessor;

    @Transactional
    public QuestionPayment questionPayment(QuestionPayment questionPayment) {
        applyCoupon(questionPayment);
        userPointManager.usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        savePaymentInformation(questionPayment);
        return questionPayment;
    }

    private void applyCoupon(QuestionPayment questionPayment) {
        if (questionPayment.isUsingCoupon()) {
            Coupon coupon = userCouponProcessor.useCoupon(questionPayment.getUserId(), questionPayment.getUserCouponId());
            questionPayment.useCoupon(coupon);
        }
    }

    private void savePaymentInformation(QuestionPayment questionPayment) {
        questionPaymentAppender.append(questionPayment);
        questionPaymentOrderAppender.createQuestionPaymentOrders(questionPayment.getOrders());
    }
}
