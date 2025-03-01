package com.eager.questioncloud.core.domain.payment.model;

import com.eager.questioncloud.core.domain.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOrderItem {
    private Long id;
    private Long questionId;
    private int price;

    public static QuestionOrderItem create(Question question) {
        return new QuestionOrderItem(null, question.getId(), question.getQuestionContent().getPrice());
    }
}