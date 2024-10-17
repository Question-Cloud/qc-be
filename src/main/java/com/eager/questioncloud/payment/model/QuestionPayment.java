package com.eager.questioncloud.payment.model;

import com.eager.questioncloud.coupon.domain.Coupon;
import com.eager.questioncloud.coupon.domain.CouponType;
import com.eager.questioncloud.payment.entity.QuestionPaymentEntity;
import com.eager.questioncloud.question.model.Question;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPayment {
    private Long id;
    private Long userId;
    private Long couponId;
    private int amount;
    private LocalDateTime createdAt;

    @Builder
    public QuestionPayment(Long id, Long userId, Long couponId, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.couponId = couponId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static QuestionPayment create(Long userId, Long couponId, List<Question> questions) {
        return QuestionPayment.builder()
            .userId(userId)
            .couponId(couponId)
            .amount(calcOriginalAmount(questions))
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
        return couponId != null;
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

    public QuestionPaymentEntity toEntity() {
        return QuestionPaymentEntity.builder()
            .id(id)
            .userId(userId)
            .couponId(couponId)
            .amount(amount)
            .createdAt(createdAt)
            .build();
    }
}
