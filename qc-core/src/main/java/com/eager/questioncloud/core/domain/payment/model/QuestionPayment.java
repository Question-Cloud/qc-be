package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
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
    private QuestionPaymentCoupon questionPaymentCoupon;
    private Long userId;
    private int amount;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, String orderId, QuestionOrder order, Long userId, QuestionPaymentCoupon questionPaymentCoupon, int amount,
        QuestionPaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.order = order;
        this.userId = userId;
        this.questionPaymentCoupon = questionPaymentCoupon;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, QuestionPaymentCoupon questionPaymentCoupon, QuestionOrder order) {
        int originalAmount = calcOriginalAmount(order);
        return QuestionPayment.builder()
            .orderId(order.getOrderId())
            .order(order)
            .userId(userId)
            .questionPaymentCoupon(questionPaymentCoupon)
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
        return questionPaymentCoupon.getUserCouponId() != null;
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
}
