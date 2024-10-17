package com.eager.questioncloud.payment.repository;

import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
import java.util.List;

public interface QuestionPaymentOrderRepository {
    List<QuestionPaymentOrder> saveAll(List<QuestionPaymentOrder> questionPaymentOrders);
}
