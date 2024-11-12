package com.eager.questioncloud.core.domain.cart.implement;

import com.eager.questioncloud.core.domain.cart.model.Cart;
import com.eager.questioncloud.core.domain.cart.model.CartItem;
import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartReader {
    private final CartItemRepository cartItemRepository;

    public Cart getUserCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        return Cart.create(userId, items);
    }
}
