package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionPaymentOrder {
    private Long id;
    private String paymentId;
    private Question question;
    private int price;

    @Builder
    public QuestionPaymentOrder(Long id, String paymentId, Question question, int price) {
        this.id = id;
        this.paymentId = paymentId;
        this.question = question;
        this.price = price;
    }

    public static List<QuestionPaymentOrder> createOrders(String paymentId, List<Question> questions) {
        return questions
            .stream()
            .map(question ->
                QuestionPaymentOrder.builder()
                    .paymentId(paymentId)
                    .question(question)
                    .price(question.getQuestionContent().getPrice())
                    .build())
            .collect(Collectors.toList());
    }
}
