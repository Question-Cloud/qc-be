package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionPaymentCoupon {
    private Long userCouponId;
    private String title;
    private CouponType couponType;
    private int value;

    public static QuestionPaymentCoupon noCoupon() {
        return new QuestionPaymentCoupon(null, null, null, 0);
    }

    public static QuestionPaymentCoupon create(Long userCouponId, Coupon coupon) {
        return new QuestionPaymentCoupon(userCouponId, coupon.getTitle(), coupon.getCouponType(), coupon.getValue());
    }

    public boolean isUsingCoupon() {
        return userCouponId != null;
    }

    public int calcDiscount(int originalAmount) {
        if (!isUsingCoupon()) {
            return originalAmount;
        }

        if (couponType.equals(CouponType.Fixed)) {
            return Math.max(originalAmount - value, 0);
        }

        if (couponType.equals(CouponType.Percent)) {
            double discountRate = value / 100.0;
            int discountAmount = (int) Math.floor(originalAmount * discountRate);
            return originalAmount - discountAmount;
        }

        throw new CoreException(Error.FAIL_USE_COUPON);
    }
}
