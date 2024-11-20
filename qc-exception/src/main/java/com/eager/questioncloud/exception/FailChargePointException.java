package com.eager.questioncloud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FailChargePointException extends RuntimeException {
    private final String paymentId;
}
