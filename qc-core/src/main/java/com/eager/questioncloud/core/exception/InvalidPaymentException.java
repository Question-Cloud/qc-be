package com.eager.questioncloud.core.exception;

import com.eager.questioncloud.core.domain.pg.PGPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidPaymentException extends RuntimeException {
    private final PGPayment pgPayment;
}
