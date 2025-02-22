package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);

    int countByUserId(Long userId);
}
