package com.eager.questioncloud.application.business.cart.service;

import com.eager.questioncloud.application.business.cart.implement.CartItemAppender;
import com.eager.questioncloud.core.domain.cart.dto.CartItemDetail;
import com.eager.questioncloud.core.domain.cart.infrastructure.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartService {
    private final CartItemAppender cartItemAppender;
    private final CartItemRepository cartItemRepository;

    public List<CartItemDetail> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void appendCartItem(Long userId, Long questionId) {
        cartItemAppender.append(userId, questionId);
    }

    public void removeCartItem(List<Long> ids, Long userId) {
        cartItemRepository.deleteByIdInAndUserId(ids, userId);
    }
}
