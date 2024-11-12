package com.eager.questioncloud.core.domain.cart.repository;

import com.eager.questioncloud.core.domain.cart.model.CartItem;
import java.util.List;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    List<CartItem> findByUserId(Long userId);

    void deleteByIdInAndUserId(List<Long> ids, Long userId);

    Boolean isExistsInCart(Long userId, Long questionId);
}
