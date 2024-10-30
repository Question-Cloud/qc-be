package com.eager.questioncloud.core.domain.payment.question.repository;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
