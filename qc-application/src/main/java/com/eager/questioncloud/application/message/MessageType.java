package com.eager.questioncloud.application.message;

import lombok.Getter;

@Getter
public enum MessageType {
    FAIL_CHARGE_POINT("fail-charge-point");

    private final String queueName;

    MessageType(String queueName) {
        this.queueName = queueName;
    }
}
