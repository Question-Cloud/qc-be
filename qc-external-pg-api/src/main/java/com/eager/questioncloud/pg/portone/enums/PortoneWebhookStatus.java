package com.eager.questioncloud.pg.portone.enums;

import lombok.Getter;

@Getter
public enum PortoneWebhookStatus {
    Ready("Ready"), Paid("Paid"), VirtualAccountIssued("VirtualAccountIssued"), PartialCancelled("PartialCancelled"), Cancelled("Cancelled"),
    Failed("Failed"), PayPending("PayPending"), CancelPending("CancelPending");
    private final String value;

    PortoneWebhookStatus(String value) {
        this.value = value;
    }
}
