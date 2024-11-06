package com.eager.questioncloud.core.domain.pg;

public interface PGAPI {
    PGPayment getPayment(String paymentId);

    void cancel(String paymentId);
}
