package com.eager.questioncloud.pg.portone.enums;

import lombok.Getter;

@Getter
public enum PortoneCancelStatus {
    FAILED("FAILED"), REQUESTED("REQUESTED"), SUCCEEDED("SUCCEEDED");
    private final String value;

    PortoneCancelStatus(String value) {
        this.value = value;
    }
}