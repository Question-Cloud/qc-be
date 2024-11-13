package com.eager.questioncloud.core.domain.payment.repository;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    void saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
