package com.eager.questioncloud.application.api.payment.point.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FailChargePointPaymentEvent {
    private String paymentId;
}
