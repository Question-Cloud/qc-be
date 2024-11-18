package com.eager.questioncloud.exception;

import com.eager.questioncloud.domain.point.PGPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidPaymentException extends RuntimeException {
    private final PGPayment pgPayment;
}
