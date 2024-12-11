package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentOrderRepository;
import com.eager.questioncloud.core.domain.payment.infrastructure.QuestionPaymentRepository;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.point.infrastructure.UserPointRepository;
import com.eager.questioncloud.core.domain.point.model.UserPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QuestionPaymentProcessor {
    private final QuestionPaymentRepository questionPaymentRepository;
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final UserPointRepository userPointRepository;

    @Transactional
    public QuestionPayment processQuestionPayment(QuestionPayment questionPayment) {
        applyCoupon(questionPayment);
        usePoint(questionPayment.getUserId(), questionPayment.getAmount());
        return savePaymentInformation(questionPayment);
    }

    private void applyCoupon(QuestionPayment questionPayment) {
        if (questionPayment.isUsingCoupon()) {
            UserCoupon userCoupon = userCouponRepository.getUserCoupon(questionPayment.getUserId());
            Coupon coupon = couponRepository.findById(userCoupon.getCouponId());
            userCoupon.use();
            questionPayment.useCoupon(coupon);
        }
    }

    private void usePoint(Long userId, int amount) {
        UserPoint userPoint = userPointRepository.getUserPoint(userId);
        userPoint.use(amount);
        userPointRepository.save(userPoint);
    }

    private QuestionPayment savePaymentInformation(QuestionPayment questionPayment) {
        questionPaymentOrderRepository.saveAll(questionPayment.getOrders());
        return questionPaymentRepository.save(questionPayment);
    }
}
