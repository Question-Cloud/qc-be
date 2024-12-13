package com.eager.questioncloud.application.api.payment.question.implement;

import com.eager.questioncloud.core.domain.coupon.infrastructure.CouponRepository;
import com.eager.questioncloud.core.domain.coupon.infrastructure.UserCouponRepository;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentCouponProcessor {
    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    public void applyCoupon(QuestionPayment questionPayment) {
        if (!questionPayment.isUsingCoupon()) {
            return;
        }

        UserCoupon userCoupon = userCouponRepository.getUserCoupon(questionPayment.getUserId());
        userCoupon.validate();

        Coupon coupon = couponRepository.findById(userCoupon.getCouponId());
        questionPayment.useCoupon(coupon);

        if (userCouponRepository.use(userCoupon.getId())) {
            throw new CustomException(Error.FAIL_USE_COUPON);
        }
    }
}
