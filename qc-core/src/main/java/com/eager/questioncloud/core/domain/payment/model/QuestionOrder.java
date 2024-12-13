package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class QuestionOrder {
    private String orderId;
    private List<QuestionOrderItem> items;

    public QuestionOrder(String orderId, List<QuestionOrderItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public static QuestionOrder createOrder(List<Question> questions) {
        String orderId = UUID.randomUUID().toString();
        List<QuestionOrderItem> items = questions
            .stream()
            .map(question -> new QuestionOrderItem(null, question.getId(), question.getQuestionContent().getPrice()))
            .collect(Collectors.toList());
        return new QuestionOrder(orderId, items);
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionOrderItem {
        private Long id;
        private Long questionId;
        private int price;
    }
}
