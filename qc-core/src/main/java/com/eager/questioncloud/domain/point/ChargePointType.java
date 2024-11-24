package com.eager.questioncloud.domain.point;

import lombok.Getter;

@Getter
public enum ChargePointType {
    PackageA(1000), PackageB(5000), PackageC(10000), PackageD(30000), PackageE(50000), PackageF(100000);
    private final int amount;

    ChargePointType(int amount) {
        this.amount = amount;
    }
}
