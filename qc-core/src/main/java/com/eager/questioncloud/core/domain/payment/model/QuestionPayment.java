package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPayment {
    private Long id;
    private QuestionOrder order;
    private QuestionPaymentCoupon questionPaymentCoupon;
    private Long userId;
    private int amount;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, QuestionOrder order, Long userId, QuestionPaymentCoupon questionPaymentCoupon, int amount,
        QuestionPaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.order = order;
        this.userId = userId;
        this.questionPaymentCoupon = questionPaymentCoupon;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, QuestionPaymentCoupon questionPaymentCoupon, QuestionOrder order) {
        return QuestionPayment.builder()
            .order(order)
            .userId(userId)
            .questionPaymentCoupon(questionPaymentCoupon)
            .amount(order.calcAmount())
            .status(QuestionPaymentStatus.SUCCESS)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public Boolean checkUsingCoupon() {
        return questionPaymentCoupon.isUsingCoupon();
    }

    public void useCoupon() {
        if (questionPaymentCoupon.getCouponType().equals(CouponType.Fixed)) {
            amount = Math.max(amount - questionPaymentCoupon.getValue(), 0);
        }
        if (questionPaymentCoupon.getCouponType().equals(CouponType.Percent)) {
            int discountAmount = (amount * (questionPaymentCoupon.getValue() / 100));
            amount = amount - discountAmount;
        }
    }

    public void fail() {
        this.status = QuestionPaymentStatus.FAIL;
    }

    public void success(Long paymentId) {
        this.id = paymentId;
    }
}
