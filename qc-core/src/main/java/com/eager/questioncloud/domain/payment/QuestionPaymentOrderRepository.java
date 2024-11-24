package com.eager.questioncloud.domain.payment;

import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
