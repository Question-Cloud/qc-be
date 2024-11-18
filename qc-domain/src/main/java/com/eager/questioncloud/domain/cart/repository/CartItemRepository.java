package com.eager.questioncloud.domain.cart.repository;

import com.eager.questioncloud.domain.cart.dto.CartItemDetail;
import com.eager.questioncloud.domain.cart.model.CartItem;
import java.util.List;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    List<CartItemDetail> findByUserId(Long userId);

    void deleteByIdInAndUserId(List<Long> ids, Long userId);

    void deleteByQuestionIdInAndUserId(List<Long> questionIds, Long userId);

    Boolean isExistsInCart(Long userId, Long questionId);
}
