package com.eager.questioncloud.payment.implement;

import com.eager.questioncloud.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.payment.repository.QuestionPaymentOrderRepository;
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
