package com.eager.questioncloud.payment;

import java.util.List;

public interface QuestionPaymentOrderRepository {
    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
