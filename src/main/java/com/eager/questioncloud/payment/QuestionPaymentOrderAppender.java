package com.eager.questioncloud.payment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentOrderAppender {
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;

    List<QuestionPaymentOrder> createQuestionPaymentOrders(List<QuestionPaymentOrder> orders) {
        return questionPaymentOrderRepository.saveAll(orders);
    }
}
