package com.eager.questioncloud.core.domain.cart.implement;

import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CartItemRemover {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void remove(Long userId, Long cartItemId) {
        cartItemRepository.deleteByIdAndUserId(cartItemId, userId);
    }
}
