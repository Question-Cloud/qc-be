package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentCoupon;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentCouponProcessor {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public QuestionPaymentCoupon getCoupon(Long userCouponId, Long userId) {
        if (userCouponId == null) {
            return QuestionPaymentCoupon.noCoupon();
        }

        UserCoupon userCoupon = userCouponRepository.getUserCoupon(userCouponId, userId);
        userCoupon.validate();

        Coupon coupon = couponRepository.findById(userCoupon.getCouponId());

        return QuestionPaymentCoupon.create(userCouponId, coupon);
    }

    public void applyCoupon(QuestionPayment questionPayment) {
        if (!questionPayment.isUsingCoupon()) {
            return;
        }

        questionPayment.useCoupon();

        if (!userCouponRepository.use(questionPayment.getQuestionPaymentCoupon().getUserCouponId())) {
            throw new CustomException(Error.FAIL_USE_COUPON);
        }
    }
}
