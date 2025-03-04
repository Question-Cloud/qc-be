package com.eager.questioncloud.pg.portone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortonePayment {
    private String id;
    private PortonePaymentStatus status;
    private PortonePaymentAmount amount;
    private String receiptUrl;
}
