package com.eager.questioncloud.payment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentOrderCreator {
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;

    List<QuestionPaymentOrder> append(List<QuestionPaymentOrder> orders) {
        return questionPaymentOrderRepository.append(orders);
    }
}
