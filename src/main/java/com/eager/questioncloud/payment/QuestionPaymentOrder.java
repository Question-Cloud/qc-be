package com.eager.questioncloud.payment;

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

    public QuestionPaymentOrderEntity toEntity() {
        return QuestionPaymentOrderEntity.builder()
            .id(id)
            .paymentId(paymentId)
            .questionId(questionId)
            .build();
    }
}
