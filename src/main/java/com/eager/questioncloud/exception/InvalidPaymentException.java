package com.eager.questioncloud.exception;

import com.eager.questioncloud.domain.portone.dto.PortonePayment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InvalidPaymentException extends RuntimeException {
    private final PortonePayment portonePayment;
}
