package com.eager.questioncloud.application.cart;

import com.eager.questioncloud.domain.cart.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    //TODO Event 처리
//    @EventListener
//    @Transactional
//    public void clearCartItem(CompletedQuestionPaymentEvent event) {
//        cartItemRepository.deleteByQuestionIdInAndUserId(event.getQuestionIds(), event.getQuestionPayment().getUserId());
//    }
}
