package com.eager.questioncloud.core.domain.cart.implement;

import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import com.eager.questioncloud.core.domain.payment.event.CompletedQuestionPaymentEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CartItemRemover {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void remove(List<Long> ids, Long userId) {
        cartItemRepository.deleteByIdInAndUserId(ids, userId);
    }

    @EventListener
    @Transactional
    public void clearCartItem(CompletedQuestionPaymentEvent event) {
        cartItemRepository.deleteByQuestionIdInAndUserId(event.getQuestionIds(), event.getQuestionPayment().getUserId());
    }
}
