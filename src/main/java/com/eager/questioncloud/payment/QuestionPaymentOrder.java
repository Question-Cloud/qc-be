package com.eager.questioncloud.payment;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPaymentOrder {
    private Long id;
    private Long paymentId;
    private Long questionId;

    @Builder
    public QuestionPaymentOrder(Long id, Long paymentId, Long questionId) {
        this.id = id;
        this.paymentId = paymentId;
        this.questionId = questionId;
    }

    public static List<QuestionPaymentOrder> createOrders(Long paymentId, List<Long> questionIds) {
        return questionIds
            .stream()
            .map(questionId -> QuestionPaymentOrder.builder()
                .paymentId(paymentId)
                .questionId(questionId)
                .build())
            .collect(Collectors.toList());
    }

    public static List<QuestionPaymentOrderEntity> toEntity(List<QuestionPaymentOrder> orders) {
        return orders
            .stream()
            .map(QuestionPaymentOrder::toEntity)
            .collect(Collectors.toList());
    }

    public QuestionPaymentOrderEntity toEntity() {
        return QuestionPaymentOrderEntity.builder()
            .id(id)
            .paymentId(paymentId)
            .questionId(questionId)
            .build();
    }
}
