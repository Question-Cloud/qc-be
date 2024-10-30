package com.eager.questioncloud.core.domain.hub.payment.repository;

import com.eager.questioncloud.core.domain.hub.payment.model.QuestionPayment;

public interface QuestionPaymentRepository {
    QuestionPayment save(QuestionPayment questionPayment);
}
