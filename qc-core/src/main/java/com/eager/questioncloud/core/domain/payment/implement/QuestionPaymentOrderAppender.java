package com.eager.questioncloud.core.domain.payment.implement;

import com.eager.questioncloud.core.domain.payment.model.QuestionPaymentOrder;
import com.eager.questioncloud.core.domain.payment.repository.QuestionPaymentOrderRepository;
import com.eager.questioncloud.core.domain.user.dto.UserPrincipal;
import com.eager.questioncloud.core.exception.CustomException;
import com.eager.questioncloud.core.exception.Error;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionPaymentOrderAppender {
    private final QuestionPaymentOrderRepository questionPaymentOrderRepository;

    public List<QuestionPaymentOrder> createQuestionPaymentOrders(List<QuestionPaymentOrder> orders) {
        checkDuplicatePurchase(orders);
        return questionPaymentOrderRepository.saveAll(orders);
    }

    private void checkDuplicatePurchase(List<QuestionPaymentOrder> orders) {
        List<Long> questionIds = orders.stream().map(QuestionPaymentOrder::getQuestionId).toList();
        Long userId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getUid();
        if (questionPaymentOrderRepository.isAlreadyPurchased(userId, questionIds)) {
            throw new CustomException(Error.ALREADY_OWN_QUESTION);
        }
    }
}
