package com.eager.questioncloud.application.cart;

import com.eager.questioncloud.domain.cart.CartItemDetail;
import com.eager.questioncloud.domain.cart.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemReader {
    private final CartItemRepository cartItemRepository;

    public List<CartItemDetail> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
