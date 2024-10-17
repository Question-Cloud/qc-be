package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
