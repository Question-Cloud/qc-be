package com.eager.questioncloud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailQuestionPaymentException extends RuntimeException {
    private String orderId;
}
