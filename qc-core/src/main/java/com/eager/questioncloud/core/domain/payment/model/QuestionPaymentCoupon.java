package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
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
}
