package com.eager.questioncloud.core.domain.cart.repository;

import com.eager.questioncloud.core.domain.cart.model.CartItem;
import java.util.List;

public interface CartRepository {
    CartItem save(CartItem cartItem);

    List<CartItem> findByUserId(Long userId);

    void deleteById(Long id);

    void deleteAllByUserid(Long userId);
}
