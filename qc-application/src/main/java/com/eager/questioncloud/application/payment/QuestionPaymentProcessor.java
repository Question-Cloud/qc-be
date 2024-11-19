package com.eager.questioncloud.application.payment;

import com.eager.questioncloud.domain.coupon.Coupon;
import com.eager.questioncloud.domain.coupon.CouponRepository;
import com.eager.questioncloud.domain.coupon.UserCoupon;
import com.eager.questioncloud.domain.coupon.UserCouponRepository;
import com.eager.questioncloud.domain.payment.QuestionPayment;
import com.eager.questioncloud.domain.payment.QuestionPaymentOrderRepository;
import com.eager.questioncloud.domain.payment.QuestionPaymentRepository;
import com.eager.questioncloud.domain.point.UserPoint;
import com.eager.questioncloud.domain.point.UserPointRepository;
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
