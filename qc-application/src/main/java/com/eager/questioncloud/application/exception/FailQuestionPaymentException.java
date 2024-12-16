package com.eager.questioncloud.application.exception;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailQuestionPaymentException extends RuntimeException {
    private QuestionPayment questionPayment;
}
