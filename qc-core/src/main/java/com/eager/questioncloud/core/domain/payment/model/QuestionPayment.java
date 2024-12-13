package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.payment.model.QuestionOrder.QuestionOrderItem;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private String orderId;
    private QuestionOrder order;
    private Long userId;
    private Long userCouponId;
    private int amount;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, String orderId, QuestionOrder order, Long userId, Long userCouponId, int amount,
        QuestionPaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.order = order;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, Long userCouponId, QuestionOrder order) {
        int originalAmount = calcOriginalAmount(order);
        return QuestionPayment.builder()
            .orderId(order.getOrderId())
            .order(order)
            .userId(userId)
            .userCouponId(userCouponId)
            .amount(originalAmount)
            .status(QuestionPaymentStatus.SUCCESS)
            .createdAt(LocalDateTime.now())
            .build();
    }

    private static int calcOriginalAmount(QuestionOrder order) {
        return order.getItems()
            .stream()
            .mapToInt(QuestionOrderItem::getPrice)
            .sum();
    }

    public Boolean isUsingCoupon() {
        return userCouponId != null;
    }

    public void useCoupon(Coupon coupon) {
        if (coupon.getCouponType().equals(CouponType.Fixed)) {
            amount = Math.max(amount - coupon.getValue(), 0);
        }
        if (coupon.getCouponType().equals(CouponType.Percent)) {
            int discountAmount = (amount * (coupon.getValue() / 100));
            amount = amount - discountAmount;
        }
    }

    public void fail() {
        this.status = QuestionPaymentStatus.FAIL;
    }
}
