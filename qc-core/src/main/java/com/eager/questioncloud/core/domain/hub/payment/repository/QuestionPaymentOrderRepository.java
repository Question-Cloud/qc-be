package com.eager.questioncloud.core.domain.hub.payment.repository;

import com.eager.questioncloud.core.domain.hub.payment.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
