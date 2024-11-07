package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentOrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentOrderAppender {
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;

    public List<QuestionPaymentOrder> createQuestionPaymentOrders(List<QuestionPaymentOrder> orders) {
        return questionPaymentOrderRepository.saveAll(orders);
    }
}
