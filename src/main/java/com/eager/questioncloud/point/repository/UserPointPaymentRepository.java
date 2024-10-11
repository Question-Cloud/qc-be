package com.eager.questioncloud.point.repository;

import com.eager.questioncloud.point.domain.UserPointPayment;

public interface UserPointPaymentRepository {
    UserPointPayment save(UserPointPayment userPointPayment);

    Boolean existsById(String paymentId);
}