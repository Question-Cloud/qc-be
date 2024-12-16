package com.eager.questioncloud.core.domain.payment.event;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailedQuestionPaymentEvent {
    private QuestionPayment questionPayment;
}
