package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
            .map(QuestionOrderItem::create)
            .collect(Collectors.toList());
        return new QuestionOrder(orderId, items);
    }

    public int calcAmount() {
        return items
            .stream()
            .mapToInt(QuestionOrderItem::getPrice)
            .sum();
    }

    public List<Long> getQuestionIds() {
        return items
            .stream()
            .map(QuestionOrderItem::getQuestionId)
            .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuestionOrderItem {
        private Long id;
        private Long questionId;
        private int price;

        public static QuestionOrderItem create(Question question) {
            return new QuestionOrderItem(null, question.getId(), question.getQuestionContent().getPrice());
        }
    }
}
