package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.domain.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
