package com.eager.questioncloud.application.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailChargePointPaymentMessage {
    private String paymentId;
}
