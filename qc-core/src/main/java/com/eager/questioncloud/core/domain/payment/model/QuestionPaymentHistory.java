package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.coupon.enums.CouponType;
import com.eager.questioncloud.core.domain.coupon.model.Coupon;
import com.eager.questioncloud.core.domain.payment.enums.QuestionPaymentStatus;
import com.eager.questioncloud.core.domain.question.dto.QuestionInformation;
import com.eager.questioncloud.core.domain.question.enums.Subject;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionPaymentHistory {
    private Long paymentId;
    private String orderId;
    private Long userId;
    private List<OrderQuestion> orders;
    private QuestionPaymentCoupon coupon;
    private int amount;
    private Boolean isUsedCoupon;
    private QuestionPaymentStatus status;
    private LocalDateTime createdAt;

    public static QuestionPaymentHistory create(QuestionPayment questionPayment, List<QuestionInformation> questions, Coupon coupon) {
        List<OrderQuestion> orders = questions.stream()
            .map(question ->
                new OrderQuestion(
                    question.getId(),
                    question.getPrice(),
                    question.getTitle(),
                    question.getThumbnail(),
                    question.getCreatorName(),
                    question.getSubject(),
                    question.getParentCategory(),
                    question.getChildCategory()))
            .toList();

        QuestionPaymentCoupon questionPaymentCoupon = coupon != null ?
            new QuestionPaymentCoupon(coupon.getTitle(), coupon.getCouponType(), coupon.getValue()) :
            new QuestionPaymentCoupon(null, null, 0);

        return QuestionPaymentHistory.builder()
            .paymentId(questionPayment.getId())
            .orderId(questionPayment.getOrderId())
            .userId(questionPayment.getUserId())
            .orders(orders)
            .coupon(questionPaymentCoupon)
            .amount(questionPayment.getAmount())
            .isUsedCoupon(questionPayment.isUsingCoupon())
            .status(questionPayment.getStatus())
            .createdAt(questionPayment.getCreatedAt())
            .build();
    }

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
