package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.vo.CouponType;
import com.eager.questioncloud.core.domain.payment.vo.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.question.vo.Subject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionPaymentHistory {
    private String paymentId;
    private List<OrderQuestion> orders;
    private QuestionPaymentCoupon coupon;
    private int amount;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    @Getter
    @AllArgsConstructor
    public static class QuestionPaymentCoupon {
        private String title;
        private CouponType couponType;
        private int value;
    }

    @Getter
    @AllArgsConstructor
    public static class OrderQuestion {
        private Long questionId;
        private int amount;
        private String title;
        private String thumbnail;
        private String creatorName;
        private Subject subject;
        private String mainCategory;
        private String subCategory;
    }
}
