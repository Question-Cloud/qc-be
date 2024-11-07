package com.eager.questioncloud.core.domain.payment.question.model;

import com.eager.questioncloud.core.domain.hub.question.model.Question;
import com.eager.questioncloud.core.domain.payment.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.coupon.vo.CouponType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private String paymentId;
    private List<QuestionPaymentOrder> orders;
    private Long userId;
    private Long userCouponId;
    private int amount;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, String paymentId, List<QuestionPaymentOrder> orders, Long userId, Long userCouponId, int amount,
        LocalDateTime createdAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.orders = orders;
        this.userId = userId;
        this.userCouponId = userCouponId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, Long userCouponId, List<Question> questions) {
        String paymentId = UUID.randomUUID().toString();
        int originalAmount = calcOriginalAmount(questions);
        List<QuestionPaymentOrder> orders = QuestionPaymentOrder.createOrders(paymentId, questions);

        return QuestionPayment.builder()
            .paymentId(paymentId)
            .orders(orders)
            .userId(userId)
            .userCouponId(userCouponId)
            .amount(originalAmount)
            .createdAt(LocalDateTime.now())
            .build();
    }

    private static int calcOriginalAmount(List<Question> questions) {
        return questions
            .stream()
            .mapToInt(question -> question.getQuestionContent().getPrice())
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
}
