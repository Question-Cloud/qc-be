package com.eager.questioncloud.domain.payment.repository;

import com.eager.questioncloud.domain.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
