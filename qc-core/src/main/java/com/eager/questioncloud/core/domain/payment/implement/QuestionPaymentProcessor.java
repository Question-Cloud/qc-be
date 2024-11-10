package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.coupon.implement.UserCouponProcessor;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentOrderRepository;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.point.implement.UserPointManager;
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
