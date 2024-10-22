package com.eager.questioncloud.domain.portone.dto;

import com.eager.questioncloud.domain.portone.enums.PortonePaymentStatus;
import lombok.Getter;

@Getter
public class PortonePayment {
    private String id;
    private PortonePaymentStatus status;
    private PortonePaymentAmount amount;
    private String receiptUrl;

    @Getter
    public static class PortonePaymentAmount {
        private int total;
        private int taxFree;
        private int vat;
    }
}
