package com.eager.questioncloud.point;

public interface UserPointPaymentRepository {
    UserPointPayment save(UserPointPayment userPointPayment);

    Boolean existsById(String paymentId);
}
