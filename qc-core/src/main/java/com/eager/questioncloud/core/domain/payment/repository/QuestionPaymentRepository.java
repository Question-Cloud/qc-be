package com.eager.questioncloud.core.domain.payment.repository;

import com.eager.questioncloud.core.domain.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
