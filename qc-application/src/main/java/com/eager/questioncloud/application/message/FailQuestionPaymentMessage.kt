package com.eager.questioncloud.application.message;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailQuestionPaymentMessage {
    private QuestionPayment questionPayment;
    private int failCount;

    public static FailQuestionPaymentMessage create(QuestionPayment questionPayment) {
        return new FailQuestionPaymentMessage(questionPayment, 0);
    }

    public void increaseFailCount() {
        this.failCount++;
    }
}
