package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class QuestionPaymentOrder {
    private String orderId;
    private List<QuestionPaymentOrderItem> items;

    public QuestionPaymentOrder(String orderId, List<QuestionPaymentOrderItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public static QuestionPaymentOrder createOrder(List<Question> questions) {
        String orderId = UUID.randomUUID().toString();
        List<QuestionPaymentOrderItem> items = questions
            .stream()
            .map(question -> new QuestionPaymentOrderItem(null, question.getId(), question.getQuestionContent().getPrice()))
            .collect(Collectors.toList());
        return new QuestionPaymentOrder(orderId, items);
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionPaymentOrderItem {
        private Long id;
        private Long questionId;
        private int price;
    }
}
