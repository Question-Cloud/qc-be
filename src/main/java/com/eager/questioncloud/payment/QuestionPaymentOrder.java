package com.eager.questioncloud.payment;

import com.eager.questioncloud.question.domain.Question;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPaymentOrder {
    private Long id;
    private Long paymentId;
    private Long questionId;
    private int price;

    @Builder
    public QuestionPaymentOrder(Long id, Long paymentId, Long questionId, int price) {
        this.id = id;
        this.paymentId = paymentId;
        this.questionId = questionId;
        this.price = price;
    }

    public static List<QuestionPaymentOrder> createOrders(Long paymentId, List<Question> questions) {
        return questions
            .stream()
            .map(question -> QuestionPaymentOrder.builder()
                .paymentId(paymentId)
                .questionId(question.getId())
                .price(question.getPrice())
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
            .price(price)
            .build();
    }
}
