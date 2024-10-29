package com.eager.questioncloud.core.exception;

import com.eager.questioncloud.core.domain.portone.dto.PortonePayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidPaymentException extends RuntimeException {
    private final PortonePayment portonePayment;
}
