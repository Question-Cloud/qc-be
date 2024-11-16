package com.eager.questioncloud.core.domain.cart.implement;

import com.eager.questioncloud.core.domain.cart.dto.CartItemInformation;
import com.eager.questioncloud.core.domain.cart.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartItemReader {
    private final CartItemRepository cartItemRepository;

    public List<CartItemInformation> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
