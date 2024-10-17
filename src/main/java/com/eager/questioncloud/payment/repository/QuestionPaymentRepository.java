package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
