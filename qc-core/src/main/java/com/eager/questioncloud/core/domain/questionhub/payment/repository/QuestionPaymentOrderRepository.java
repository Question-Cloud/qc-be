package com.eager.questioncloud.core.domain.questionhub.payment.repository;

import com.eager.questioncloud.core.domain.questionhub.payment.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
