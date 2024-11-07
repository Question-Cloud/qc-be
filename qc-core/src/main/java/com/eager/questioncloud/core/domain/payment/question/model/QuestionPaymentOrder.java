package com.eager.questioncloud.core.domain.payment.question.model;

import com.eager.questioncloud.core.domain.hub.question.model.Question;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPaymentOrder {
    private Long id;
    private String paymentId;
    private Long questionId;
    private int price;

    @Builder
    public QuestionPaymentOrder(Long id, String paymentId, Long questionId, int price) {
        this.id = id;
        this.paymentId = paymentId;
        this.questionId = questionId;
        this.price = price;
    }

    public static List<QuestionPaymentOrder> createOrders(String paymentId, List<Question> questions) {
        return questions
            .stream()
            .map(question -> QuestionPaymentOrder.builder()
                .paymentId(paymentId)
                .questionId(question.getId())
                .price(question.getQuestionContent().getPrice())
                .build())
            .collect(Collectors.toList());
    }
}
