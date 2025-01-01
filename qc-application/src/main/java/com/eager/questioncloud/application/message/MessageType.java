package com.eager.questioncloud.application.message;

import lombok.Getter;

@Getter
public enum MessageType {
    FAIL_CHARGE_POINT("fail-charge-point"), FAIL_QUESTION_PAYMENT("fail-question-payment");

    private final String queueName;

    MessageType(String queueName) {
        this.queueName = queueName;
    }
}
