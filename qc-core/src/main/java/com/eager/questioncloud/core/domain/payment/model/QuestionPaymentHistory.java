package com.eager.questioncloud.core.domain.payment.model;

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

    public static QuestionPaymentHistory create(QuestionPayment questionPayment, List<QuestionInformation> questions) {
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

        return QuestionPaymentHistory.builder()
            .paymentId(questionPayment.getId())
            .orderId(questionPayment.getOrder().getOrderId())
            .userId(questionPayment.getUserId())
            .orders(orders)
            .coupon(questionPayment.getQuestionPaymentCoupon())
            .amount(questionPayment.getAmount())
            .isUsedCoupon(questionPayment.isUsingCoupon())
            .status(questionPayment.getStatus())
            .createdAt(questionPayment.getCreatedAt())
            .build();
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
