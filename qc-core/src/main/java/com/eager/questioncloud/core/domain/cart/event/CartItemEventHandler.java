package com.eager.questioncloud.core.domain.cart.event;

import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemEventHandler {
    private final CartItemRepository cartItemRepository;

    @EventListener
    public void clearCartItem(ClearCartItemEvent clearCartItemEvent) {
        cartItemRepository.deleteByQuestionIdInAndUserId(clearCartItemEvent.getQuestionIds(), clearCartItemEvent.getUserId());
    }
}
