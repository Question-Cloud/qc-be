package com.eager.questioncloud.core.domain.payment.question.implement;

import com.eager.questioncloud.core.domain.payment.question.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.payment.question.repository.QuestionPaymentOrderRepository;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentOrderAppender {
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;

    public List<QuestionPaymentOrder> createQuestionPaymentOrders(Long userId, List<QuestionPaymentOrder> orders) {
        checkDuplicatePurchase(userId, orders);
        return questionPaymentOrderRepository.saveAll(orders);
    }

    private void checkDuplicatePurchase(Long userId, List<QuestionPaymentOrder> orders) {
        List<Long> questionIds = orders.stream().map(QuestionPaymentOrder::getQuestionId).toList();
        if (questionPaymentOrderRepository.isAlreadyPurchased(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
    }
}
