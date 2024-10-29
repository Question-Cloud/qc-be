package com.eager.questioncloud.core.domain.questionhub.payment.repository;

import com.eager.questioncloud.core.domain.questionhub.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
