package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.coupon.model.UserCoupon;
import com.eager.questioncloud.core.domain.coupon.vo.CouponType;
import com.eager.questioncloud.core.domain.payment.vo.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.question.model.Question;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private Long userId;
    private String paymentId;
    private List<QuestionPaymentOrder> orders;
    private UserCoupon userCoupon;
    private int amount;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, String paymentId, List<QuestionPaymentOrder> orders, Long userId, UserCoupon userCoupon, int amount,
        QuestionPaymentStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.orders = orders;
        this.userId = userId;
        this.userCoupon = userCoupon;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, UserCoupon userCoupon, List<Question> questions) {
        String paymentId = UUID.randomUUID().toString();
        int originalAmount = calcOriginalAmount(questions);
        List<QuestionPaymentOrder> orders = QuestionPaymentOrder.createOrders(paymentId, questions);

        return QuestionPayment.builder()
            .paymentId(paymentId)
            .orders(orders)
            .userId(userId)
            .userCoupon(userCoupon)
            .amount(originalAmount)
            .status(QuestionPaymentStatus.SUCCESS)
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
        return userCoupon != null;
    }

    public void useCoupon() {
        Coupon coupon = userCoupon.getCoupon();
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
