package com.eager.questioncloud.application.cart;

import com.eager.questioncloud.domain.cart.CartItemDetail;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartService {
    private final CartItemReader cartItemReader;
    private final CartItemAppender cartItemAppender;
    private final CartItemRemover cartItemRemover;

    public List<CartItemDetail> getCartItems(Long userId) {
        return cartItemReader.getCartItems(userId);
    }

    public void appendCartItem(Long userId, Long questionId) {
        cartItemAppender.append(userId, questionId);
    }

    public void removeCartItem(List<Long> ids, Long userId) {
        cartItemRemover.remove(ids, userId);
    }
}
