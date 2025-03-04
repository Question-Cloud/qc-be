package com.eager.questioncloud.application.business.payment.question.event;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuestionPaymentEvent {
    private QuestionPayment questionPayment;
}
