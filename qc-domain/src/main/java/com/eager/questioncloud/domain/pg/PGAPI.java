package com.eager.questioncloud.domain.pg;

public interface PGAPI {
    PGPayment getPayment(String paymentId);

    void cancel(String paymentId);
}
