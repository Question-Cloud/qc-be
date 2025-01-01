package com.eager.questioncloud.application.api.payment.question.event;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailQuestionPaymentEvent {
    private QuestionPayment questionPayment;
}
