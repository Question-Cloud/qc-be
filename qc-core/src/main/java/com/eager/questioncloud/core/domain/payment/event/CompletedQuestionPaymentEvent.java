package com.eager.questioncloud.core.domain.payment.event;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import java.util.List;
import lombok.Getter;

@Getter
public class CompletedQuestionPaymentEvent {
    private final QuestionPayment questionPayment;
    private final List<Long> questionIds;

    private CompletedQuestionPaymentEvent(QuestionPayment questionPayment, List<Long> questionIds) {
        this.questionIds = questionIds;
        this.questionPayment = questionPayment;
    }

    public static CompletedQuestionPaymentEvent create(QuestionPayment questionPayment, List<Long> questionIds) {
        return new CompletedQuestionPaymentEvent(questionPayment, questionIds);
    }
}
