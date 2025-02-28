package com.eager.questioncloud.application.business.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.repository.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentCouponReader {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public QuestionPaymentCoupon getQuestionPaymentCoupon(Long userCouponId, Long userId) {
        if (userCouponId == null) {
            return QuestionPaymentCoupon.noCoupon();
        }

        UserCoupon userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId);
        userCoupon.validate();

        Coupon coupon = couponRepository.findById(userCoupon.getCouponId());

        return QuestionPaymentCoupon.create(userCouponId, coupon);
    }
}
