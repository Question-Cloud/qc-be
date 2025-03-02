package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;

public interface QuestionOrderRepository {
    void save(QuestionOrder questionOrder);

    void deleteAllInBatch();
}
