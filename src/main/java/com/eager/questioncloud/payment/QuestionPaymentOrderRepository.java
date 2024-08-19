package com.eager.questioncloud.payment;

import java.util.List;

public interface QuestionPaymentOrderRepository {
    List<QuestionPaymentOrder> append(List<QuestionPaymentOrder> questionPaymentOrders);
}
