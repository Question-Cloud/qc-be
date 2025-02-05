package com.eager.questioncloud.core.domain.payment.infrastructure.repository;

import com.eager.questioncloud.core.domain.payment.model.QuestionOrder;
import java.util.List;

public interface QuestionOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    void save(QuestionOrder questionOrder);

    void deleteAllInBatch();
}
