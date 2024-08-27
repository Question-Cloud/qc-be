package com.eager.questioncloud.point;

public interface UserPointPaymentRepository {
    UserPointPayment append(UserPointPayment userPointPayment);

    Boolean existsById(String paymentId);
}
