package com.eager.questioncloud.core.domain.payment.question.repository;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    Boolean isAlreadyPurchased(Long userId, List<Long> questionIds);

    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
